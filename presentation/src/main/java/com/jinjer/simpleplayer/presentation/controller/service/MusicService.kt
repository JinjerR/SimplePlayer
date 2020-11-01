package com.jinjer.simpleplayer.presentation.controller.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.*
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.media.session.MediaButtonReceiver
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jinjer.simpleplayer.domain.usecases.GetTrackByIdUseCase
import com.jinjer.simpleplayer.domain.usecases.GetTracksUseCase
import com.jinjer.simpleplayer.presentation.R
import com.jinjer.simpleplayer.presentation.models.track.TrackDataMapper
import com.jinjer.simpleplayer.presentation.utils.SpConstants.keyCurrentTrack
import com.jinjer.simpleplayer.presentation.utils.ShowLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.jinjer.simpleplayer.presentation.controller.service.AppEvent.*
import com.jinjer.simpleplayer.presentation.controller.service.PlayerState.*
import com.jinjer.simpleplayer.presentation.models.track.TrackData
import com.jinjer.simpleplayer.presentation.utils.Utils
import com.jinjer.simpleplayer.presentation.utils.notifications.INotifyManager
import com.jinjer.simpleplayer.presentation.utils.notifications.NotificationInfo
import kotlinx.coroutines.withContext
import org.kodein.di.*
import org.kodein.di.android.closestDI
import java.lang.ref.WeakReference

class MusicService: Service(),
    IPlayerStateChangeListener, DIAware {

    override val di: DI by closestDI ()

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var player: IPlayer
    private lateinit var playerNavigator: IPlayerNavigator

    private var appInBackground = false
    private var clientMessenger: Messenger? = null
    private var lockedSkippingTracks = false

    private val simpleName = MusicService::class.java.simpleName
    private val getTracks: GetTracksUseCase by instance()
    private val getTrackById: GetTrackByIdUseCase by instance()
    private val trackMapper: TrackDataMapper by instance()
    private val notifyManager: INotifyManager by instance()
    private val serviceScope = CoroutineScope(Dispatchers.Main)
    private val mMessenger = Messenger(ServiceHandler(this))
    private val currentPlaybackData = Bundle()

    private val currentPlaybackState: Int
        get() {
            return mediaSession
                .controller
                ?.playbackState
                ?.state
                ?: STATE_NONE
        }

    private val currentPosition: Long
    get() = player.currentPosition.toLong()

    private val playbackState = Builder().setActions(
        ACTION_PLAY or
                ACTION_PAUSE or
                ACTION_STOP or
                ACTION_PLAY_PAUSE or
                ACTION_SKIP_TO_NEXT or
                ACTION_SKIP_TO_PREVIOUS or
                ACTION_SEEK_TO or
                ACTION_PLAY_FROM_MEDIA_ID or
                ACTION_PREPARE_FROM_MEDIA_ID
    )

    private val mediaSessionCallback: MediaSessionCompat.Callback = object : MediaSessionCompat.Callback() {
        override fun onPlay() {
            ShowLog.i("$simpleName.mediaSessionCallback.onPlay()", tagMusicControl)
            player.resume()
        }

        override fun onPause() {
            ShowLog.i("$simpleName.mediaSessionCallback.onPause()", tagMusicControl)
            player.pause()
        }

        override fun onStop() {
            ShowLog.i("$simpleName.mediaSessionCallback.onStop()", tagMusicControl)
            player.stop()
        }

        override fun onSeekTo(pos: Long) {
            ShowLog.i("$simpleName.mediaSessionCallback.onSeekTo() pos = $pos", tagMusicControl)
            player.seekTo(pos.toInt())
        }

        override fun onSkipToNext() {
            ShowLog.i("$simpleName.mediaSessionCallBack.onSkipToNext()", tagMusicControl)
            skipToNext(false)
        }

        override fun onSkipToPrevious() {
            if (lockedSkippingTracks) {
                return
            }

            ShowLog.i("$simpleName.mediaSessionCallback.onSkipToPrevious()", tagMusicControl)

            val previousTrackId = playerNavigator.previousTrack()?: return
            if (player.isPlaying) {
                player.play(previousTrackId)
            } else {
                player.prepareForPlaying(previousTrackId)
            }
        }

        override fun onPrepareFromMediaId(mediaId: String?, extras: Bundle?) {
            ShowLog.i("$simpleName.mediaSessionCallback.onPrepareFromMediaId()", tagMusicControl)
            val trackId = mediaId?.toInt() ?: return

            with(playerNavigator) {
                setTrack(trackId)
                currentTrack()?.let { player.prepareForPlaying(it) }
            }
        }

        override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {
            ShowLog.i("$simpleName.mediaSessionCallback.onPlayFromMediaId() mediaId = $mediaId, extras = $extras", tagMusicControl)

            val trackId = mediaId?.toIntOrNull() ?: run {
                ShowLog.w("$simpleName.onPlayFromMediaId() track id is null", tagMusicControl)
                return
            }

            playerNavigator.setTrack(trackId)
            player.play(trackId)
        }

        override fun onCustomAction(action: String?, extras: Bundle?) {
            ShowLog.i("$simpleName.mediaSessionCallback.onCustomAction() action = $action, extras = $extras", tagMusicControl)

            if (action == actionUpdateQueue) {
                extras?.getParcelable<QueueData>(PlayerNavigator.keyQueueData)?.let { queueData ->
                    if (queueData.type == QueueType.SEARCH) {
                        lockSkippingTracks()
                    } else {
                        unlockSkippingTracks()
                    }

                    playerNavigator.setQueue(queueData)
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        ShowLog.i("$simpleName.onCreate()", tagMusicControl)

        loadTracks()
        setupMediaSession()
        player = Player(applicationContext, direct.instance(),this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ShowLog.i("$simpleName.onDestroy()", tagMusicControl)

        player.onAppEventHappened(APP_CLOSES)
        mediaSession.isActive = false
        mediaSession.release()

        if (appInBackground) {
            stopForeground(true)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        ShowLog.i("$simpleName.onBind()", tagMusicControl)
        return mMessenger.binder
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
        ShowLog.i("$simpleName.onRebind()", tagMusicControl)

        // deliver fresh data to ui
        changePlaybackState(currentPlaybackState, currentPosition)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        ShowLog.i("$simpleName.onUnbind()", tagMusicControl)
        return true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        MediaButtonReceiver.handleIntent(mediaSession, intent)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            appInBackground = true

            if (player.isPlaying.not()) {
                return
            }
            updateNotification(startForeground = true)
            player.onAppEventHappened(GOES_INTO_BACKGROUND)
        }
    }

    override fun onPlayerStateChanged(playerState: PlayerState) {
        ShowLog.i("$simpleName.onPlayerStateChanged() playerState = $playerState", tagMusicControl)

        when(playerState) {
            PREPARED, STARTED, PAUSED -> {
                val currentTrackId = playerNavigator.currentTrack() ?: return
                currentPlaybackData.putInt(keyCurrentTrack, currentTrackId)

                val newPosition = if (playerState == PREPARED) {
                    0L
                } else {
                    currentPosition
                }

                updateNotification()

                val newPlaybackState = PlayerState.convertToPlaybackState(playerState)
                changePlaybackState(newPlaybackState, newPosition)
            }
            COMPLETED -> {
                skipToNext(true)
            }
            else -> { }
        }
    }

    override fun onTrackPositionChanged(newPosition: Int) {
        changePlaybackState(currentPlaybackState, newPosition.toLong())
    }

    private fun updateNotification(startForeground: Boolean = false) {
        if (startForeground.not() && appInBackground.not()) {
            return
        }

        serviceScope.launch {
            val trackId = playerNavigator.currentTrack() ?: return@launch
            val currentTrack = getTrackById(trackId)?.let { trackMapper.from(it) } ?: return@launch
            val notifyInfo = buildNotificationInfo(currentTrack)

            if (startForeground) {
                startForeground(notifyManager.getNotificationId(), notifyManager.getNotification(notifyInfo))
            } else {
                notifyManager.updateNotification(notifyInfo)
            }
        }
    }

    private suspend fun buildNotificationInfo(track: TrackData): NotificationInfo = withContext(Dispatchers.Default) {
        fun loadNotificationThumbnail(uri: Uri): Bitmap? {
            return try {
                Glide.with(applicationContext)
                    .asBitmap()
                    .load(uri)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.icon_empty_album_art)
                    .submit()
                    .get()
            } catch (e: Exception) {
                val drawable = ContextCompat.getDrawable(applicationContext, R.drawable.icon_empty_album_art)
                return drawable!!.toBitmap()
            }
        }

        val albumUri = Utils.getAlbumArtUri(track.albumId.toLong())
        val bitmap = loadNotificationThumbnail(albumUri)

        return@withContext NotificationInfo(
            track.title,
            track.artist,
            player.isPlaying,
            bitmap,
            mediaSession.sessionToken)
    }

    private fun skipToNext(afterCompletion: Boolean) {
        if (lockedSkippingTracks) {
            if (afterCompletion) {
                playerNavigator.currentTrack()?.let {
                    player.repeatCurrentTrack()
                }
            }
            return
        }

        val nextTrackId = playerNavigator.nextTrack()?: return
        if (player.isPlaying || afterCompletion) {
            player.play(nextTrackId)
        } else {
            player.prepareForPlaying(nextTrackId)
        }
    }

    private fun loadTracks() {
        serviceScope.launch {
            val trackIds = getTracks()?.map { it.id } ?: emptyList()
            playerNavigator = PlayerNavigator(trackIds)

            ShowLog.i("$simpleName.loadTracks() ${trackIds.size} tracks loaded", tagMusicControl)
        }
    }

    private fun changePlaybackState(newState: Int, position: Long) {
        val state = playbackState.setState(
            newState,
            position,
            0f
        )

        state.setExtras(currentPlaybackData)

        mediaSession.setPlaybackState(state.build())
    }

    private fun setupMediaSession() {
        val mediaButtonIntent = Intent(
            Intent.ACTION_MEDIA_BUTTON, null, applicationContext,
            MediaButtonReceiver::class.java
        )

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            0,
            mediaButtonIntent,
            0
        )

        mediaSession = MediaSessionCompat(applicationContext, tagMusicControl).apply {
            setCallback(mediaSessionCallback)
            setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS or MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS)
            setMediaButtonReceiver(pendingIntent)
            setPlaybackState(playbackState.setState(STATE_NONE, 0L, 0f).build())
        }
    }

    private fun transmitTokenToClient() {
        sendMessageToClient(actionSessionToken, mediaSession.sessionToken)
    }

    private fun sendMessageToClient(action: Int, data: Any? = null) {
        val msg = Message.obtain(null, action, data)
        try {
            clientMessenger?.send(msg)
        } catch (e: RemoteException) {
            ShowLog.e("$simpleName.sendMessageToClient(), ${ e.printStackTrace() }")
        }
    }

    private fun lockSkippingTracks() {
        lockedSkippingTracks = true
    }

    private fun unlockSkippingTracks() {
        lockedSkippingTracks = false
    }

    class ServiceHandler(musicService: MusicService): Handler() {
        private val musicServiceWeak = WeakReference(musicService)

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            musicServiceWeak.get()?.apply {
                ShowLog.i("$simpleName.ServiceHandler.handleMessage(), what = ${ msg.what }", tagMusicControl)

                when(msg.what) {
                    actionSessionToken -> {
                        clientMessenger = msg.replyTo
                        transmitTokenToClient()
                    }
                    actionAppResumes -> {
                        if (appInBackground) {
                            appInBackground = false
                            stopForeground(true)
                            player.onAppEventHappened(RETURN_FROM_BACKGROUND)
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val tagMusicControl = "tag_music_control"
        const val actionUpdateQueue = "action_update_queue"
        const val actionSessionToken = 1
        const val actionAppResumes = 4
    }
}