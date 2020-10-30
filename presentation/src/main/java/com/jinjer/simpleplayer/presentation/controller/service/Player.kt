package com.jinjer.simpleplayer.presentation.controller.service

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import android.os.Looper
import com.jinjer.simpleplayer.presentation.controller.service.AppEvent.*
import com.jinjer.simpleplayer.presentation.controller.service.MusicService.Companion.tagMusicControl
import com.jinjer.simpleplayer.presentation.controller.service.PlayerState.*
import com.jinjer.simpleplayer.presentation.utils.ShowLog
import com.jinjer.simpleplayer.presentation.utils.Utils

// TODO: implement OnSeekCompleteListener

// *state_diagram* [https://developer.android.com/reference/android/media/MediaPlayer#state-diagram]

/**
 * Main conclusions from the state diagram
 *  1) The media player can be in one of the states shown in the diagram, @see [PlayerState]
 *  2) When a MediaPlayer object is just created or after [MediaPlayer.reset] is called, it is in the [IDLE] state
 *  3) Calling almost any method other than [MediaPlayer.setDataSource] in [IDLE] state will cause an error
 *  3.1) When the MediaPlayer is in the [IDLE] state after the object is created, in case of an error,
 *   the [MediaPlayer.OnErrorListener] will not be called, and the object will remain in the [IDLE] state
 *  3.2) When the MediaPlayer is in [IDLE] state after [MediaPlayer.reset], in case of error,
 *   [MediaPlayer.OnErrorListener] will be called and the object will go into [ERROR] state
 *  4) To get the media player out of the [ERROR] state, you need to call [MediaPlayer.reset] method,
 *   then the object will go into the [IDLE] state
 *  5) If the object is in the [STOPPED] state, then it can be started only after it is in the [PREPARED] state,
 *   i.e. after [MediaPlayer.prepare], or [MediaPlayer.prepareAsync]
 *  6) After calling [MediaPlayer.release], the object can no longer be used
 **/

class Player(private val context: Context, private val stateChangeListener: IPlayerStateChangeListener):
    MediaPlayer.OnPreparedListener,
    MediaPlayer.OnErrorListener,
    MediaPlayer.OnCompletionListener,
    AudioManager.OnAudioFocusChangeListener,
    IPlayer {

    private val periodToUpdate = 1000L
    private val emptyTrackId = -1
    private val simpleName = Player::class.java.simpleName
    private val audioManager: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val runnableTrackPositionChanged = Runnable(::notifyTrackPositionChanged)
    private val handlerMain = Handler(Looper.getMainLooper())

    override val currentPosition: Int
        get() {
            return if (playerState == IDLE) 0 else mediaPlayer?.currentPosition ?: 0
        }

    override val isPlaying: Boolean
        get() = mediaPlayer?.isPlaying ?: false

    private var playerState: PlayerState = IDLE

    private var mediaPlayer: MediaPlayer? = MediaPlayer().apply {
        setOnErrorListener(this@Player)
        setOnCompletionListener(this@Player)
        setOnPreparedListener(this@Player)
    }

    private var appInBackground = false
    private var playAfterPrepare: Boolean = false
    private var currentTrackId = emptyTrackId
    private var audioFocusRequest: AudioFocusRequest? = null

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()

            audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setOnAudioFocusChangeListener(this)
                .setAcceptsDelayedFocusGain(false)
                .setWillPauseWhenDucked(true)
                .setAudioAttributes(audioAttributes)
                .build()
        }
    }

    override fun getCurrentlyPlayingTrackId(): Int = currentTrackId

    override fun onAppEventHappened(event: AppEvent) {
        ShowLog.i("$simpleName.onAppEventHappened() event = $event", tagMusicControl)

        when(event) {
            GOES_INTO_BACKGROUND, RETURN_FROM_BACKGROUND -> {
                appInBackground = event == GOES_INTO_BACKGROUND
                updatePositionBehavior()
            }
            APP_CLOSES -> {
                resetInternal()
                mediaPlayer?.release()
                mediaPlayer = null
                audioManager.abandonAudioFocus(this)
            }
        }
    }

    override fun prepareForPlaying(trackId: Int) {
        prepareInternal(trackId, false)
    }

    override fun play(trackId: Int) {
        when(playerState) {
            IDLE, INITIALIZED, STOPPED -> {
                prepareInternal(trackId, true)
            }
            STARTED, PAUSED, PREPARED, COMPLETED -> {
                if (currentTrackId != trackId) {
                    resetInternal()
                    prepareInternal(trackId, true)
                } else {
                    startInternal()
                }
            }
            else -> {
                ShowLog.w("$simpleName.play() called in state: $playerState", tagMusicControl)
            }
        }
    }

    override fun resume() {
        startInternal()
    }

    override fun pause() {
        pauseInternal()
    }

    override fun stop() {
        stopInternal()
    }

    override fun seekTo(position: Int) {
        seekToInternal(position)
    }

    override fun onPrepared(mp: MediaPlayer?) {
        if (playAfterPrepare) {
            updatePlayerState(PREPARED, false)
            startInternal()
        } else {
            updatePlayerState(PREPARED, true)
        }
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        ShowLog.e("$simpleName.onError() mp = $mp, what = $what, extra = $extra", tagMusicControl)

        updatePlayerState(ERROR)

        // TODO: firebase
        val mediaPlayerException = MediaPlayerException(what, extra, playerState)
//        FirebaseCrashlytics.getInstance().recordException(mediaPlayerException)

        resetInternal()

        return true
    }

    override fun onCompletion(mp: MediaPlayer?) {
        updatePlayerState(COMPLETED)
    }

    override fun onAudioFocusChange(focusChange: Int) {
        when(focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> startInternal()
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> pauseInternal()
            else -> { }
        }
    }

    private fun setDataSourceInternal(trackId: Int) {
        val mp = mediaPlayer ?: return

        val songUri = Utils.getSongUri(trackId.toLong())
        try {
            mp.setDataSource(context, songUri)
            updatePlayerState(INITIALIZED)
        } catch (e: Exception) {
            resetInternal()
        }
    }

    private fun prepareInternal(trackId: Int, playAfterPrepare: Boolean) {
        if (trackId == emptyTrackId) {
            return
        }

        when(playerState) {
            IDLE -> {
                setDataSourceInternal(trackId)
                currentTrackId = trackId

                mediaPlayer?.prepareAsync()
                updatePlayerState(PREPARING)
                this.playAfterPrepare = playAfterPrepare
            }
            PAUSED, PREPARED -> {
                if (currentTrackId != trackId) {
                    resetInternal()
                    prepareInternal(trackId, false)
                }
            }
            INITIALIZED, STOPPED -> {
                mediaPlayer?.prepareAsync()
                updatePlayerState(PREPARING)
                this.playAfterPrepare = playAfterPrepare
            }
            else -> {
                ShowLog.w("$simpleName.prepareForPlaying() called in wrong state: player state = $playerState", tagMusicControl)
            }
        }
    }

    private fun resetInternal() {
        val mp = mediaPlayer ?: return

        mp.reset()
        updatePlayerState(IDLE)
    }

    private fun stopInternal() {
        val mp = mediaPlayer ?: return

        when(playerState) {
            PREPARED, STARTED, PAUSED -> {
                mp.stop()
                updatePlayerState(STOPPED)
                updatePositionBehavior()
            }
            else -> {
                ShowLog.w("$simpleName.stop() called in wrong state, playerState = $playerState", tagMusicControl)
            }
        }
    }

    private fun startInternal() {
        val mp = mediaPlayer ?: return

        if (audioFocusGranted().not()) {
            return
        }

        when(playerState) {
            PREPARED, PAUSED -> {
                mp.start()
                updatePlayerState(STARTED)
                updatePositionBehavior()
            }
            else -> {
                ShowLog.w("$simpleName.startInternal() called in wrong state, playerState = $playerState", tagMusicControl)
            }
        }
    }

    private fun pauseInternal() {
        val mp = mediaPlayer ?: return

        if (playerState == STARTED) {
            mp.pause()
            updatePlayerState(PAUSED)
            updatePositionBehavior()
        } else {
            ShowLog.w("$simpleName.pause() called in wrong state, playerState = $playerState", tagMusicControl)
        }
    }

    private fun seekToInternal(position: Int) {
        val mp = mediaPlayer ?: return

        when(playerState) {
            PREPARED, STARTED, PAUSED -> {
                mp.seekTo(position)
            }
            else -> {
                ShowLog.w("$simpleName.seekToInternal() called in wrong state, playerState = $playerState", tagMusicControl)
            }
        }
    }

    private fun audioFocusGranted(): Boolean {
        val requestResult = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager.requestAudioFocus(audioFocusRequest!!)
        } else {
            audioManager.requestAudioFocus(
                this,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN
            )
        }

        return requestResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
    }

    private fun notifyTrackPositionChanged() {
        stateChangeListener.onTrackPositionChanged(currentPosition)
        handlerMain.postDelayed(runnableTrackPositionChanged, periodToUpdate)
    }

    private fun startUpdatePosition() {
        ShowLog.i("$simpleName.startUpdatePosition()", tagMusicControl)
        handlerMain.post(runnableTrackPositionChanged)
    }
    private fun stopUpdatePosition() {
        ShowLog.i("$simpleName.stopUpdatePosition()", tagMusicControl)
        handlerMain.removeCallbacks(runnableTrackPositionChanged)
    }

    private fun updatePositionBehavior() {
        if (appInBackground) {
            stopUpdatePosition()
            return
        }

        if (isPlaying) {
            startUpdatePosition()
        } else {
            stopUpdatePosition()
        }
    }

    private fun updatePlayerState(newState: PlayerState, callListener: Boolean = true) {
        playerState = newState
        if (callListener) {
            stateChangeListener.onPlayerStateChanged(newState)
        }
    }
}