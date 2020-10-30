package com.jinjer.simpleplayer.presentation.controller.main

import android.os.Bundle
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jinjer.simpleplayer.domain.usecases.GetCurrentTrackIdUseCase
import com.jinjer.simpleplayer.domain.usecases.GetTrackByIdUseCase
import com.jinjer.simpleplayer.domain.usecases.GetTracksUseCase
import com.jinjer.simpleplayer.presentation.base.SimplePlayerApp
import com.jinjer.simpleplayer.presentation.controller.client.IClientCallback
import com.jinjer.simpleplayer.presentation.utils.SpConstants
import com.jinjer.simpleplayer.presentation.controller.client.MediaClientManager
import com.jinjer.simpleplayer.presentation.controller.service.MusicService
import com.jinjer.simpleplayer.presentation.controller.service.MusicService.Companion.tagMusicControl
import com.jinjer.simpleplayer.presentation.controller.service.PlayerNavigator
import com.jinjer.simpleplayer.presentation.controller.service.QueueData
import com.jinjer.simpleplayer.presentation.models.Track
import com.jinjer.simpleplayer.presentation.models.mappers.TrackMapper
import com.jinjer.simpleplayer.presentation.utils.ShowLog
import kotlinx.coroutines.launch

class MainViewModel(
    app: SimplePlayerApp,
    private val getTracks: GetTracksUseCase,
    private val getCurrentTrackId: GetCurrentTrackIdUseCase,
    private val getTrackById: GetTrackByIdUseCase,
    private val mapper: TrackMapper): AndroidViewModel(app), IClientCallback,
    IPlayerController {

    private val tagMainViewModel = "tag_main_view_model"
    private val simpleName = MainViewModel::class.java.simpleName
    private val appContext = getApplication<SimplePlayerApp>().applicationContext
    private val mediaClientManager = MediaClientManager(appContext, this)
    private var tracks: List<Track>? = null

    private var mediaController: MediaControllerCompat? = null
    private var mediaControllerWasRegistered = false

    private val playerControls: MediaControllerCompat.TransportControls
    get() = mediaController!!.transportControls

    private val mCurrentTrack = MutableLiveData( Track.emptyTrack() )
    override val currentTrack: LiveData<Track> = mCurrentTrack

    private val mCurrentPosition = MutableLiveData(0L)
    override val currentPosition: LiveData<Long> = mCurrentPosition

    private val mIsPlaying = MutableLiveData(false)
    override val isPlaying: LiveData<Boolean> = mIsPlaying

    private val mIsTracksLoaded = MutableLiveData(false)
    override val isTracksLoaded: LiveData<Boolean> = mIsTracksLoaded

    private var permissionWasGranted = false
    private var currentQueueData = QueueData.buildAllTracksData()

    override fun onTokenReceived(token: MediaSessionCompat.Token) {
        mediaController = MediaControllerCompat(appContext, token)
        registerMediaController()

        getCurrentTrackId().let { id ->
            val trackId = if (id == -1) tracks?.firstOrNull()?.id else id
            trackId?.let {
                playerControls.prepareFromMediaId(it.toString(), null)
            }
        }
    }

    override fun onCleared() {
        unregisterMediaController()
        mediaClientManager.unbind()
        mediaClientManager.stopService()
    }

    override fun play(trackId: Int, playbackQueue: QueueData) {
        if (checkQueueTheSame(playbackQueue).not()) {
            updateQueue(playbackQueue)
            currentQueueData = playbackQueue
        }

        if (trackId == mCurrentTrack.value?.id) {
            resume()
            return
        }

        playerControls.playFromMediaId(trackId.toString(), null)
    }

    override fun resume() {
        if (mIsPlaying.value == false) {
            playerControls.play()
        } else {
            playerControls.pause()
        }
    }

    override fun seekTo(position: Int) {
        playerControls.seekTo(position * 1000L)
    }

    override fun skipToNext() {
        playerControls.skipToNext()
    }

    override fun skipToPrevious() {
        playerControls.skipToPrevious()
    }

    fun onPermissionGranted() {
        permissionWasGranted = true

        viewModelScope.launch {
            tracks = mapper.fromList(getTracks())

            if (tracks.isNullOrEmpty().not()) {
                mediaClientManager.startService()
                mediaClientManager.bind()
                mIsTracksLoaded.value = true
            }
        }
    }

    fun onAppResumes() {
        ShowLog.i("$simpleName.onAppResumes()", tagMusicControl)

        if (permissionWasGranted.not()) {
            return
        }

        registerMediaController()
        mediaClientManager.bind()
        mediaClientManager.sendMessageToService(MusicService.actionAppResumes)
    }

    fun onAppStopped() {
        ShowLog.i("$simpleName.onAppStopped()", tagMusicControl)

        if (permissionWasGranted.not()) {
            return
        }

        unregisterMediaController()
        mediaClientManager.unbind()
    }

    private fun registerMediaController() {
        mediaController?.let { controller ->
            if (mediaControllerWasRegistered.not()) {
                controller.registerCallback(mediaControllerCallback)
                mediaControllerWasRegistered = true
            }
        }
    }

    private fun unregisterMediaController() {
        mediaController?.let { controller ->
            if (mediaControllerWasRegistered) {
                controller.unregisterCallback(mediaControllerCallback)
                mediaControllerWasRegistered = false
            }
        }
    }

    private val mediaControllerCallback = object: MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged(playbackState: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(playbackState)

            val playback = playbackState ?: return
            val currentTrackId: Int = playback.extras?.getInt(SpConstants.keyCurrentTrack) ?: return

            if (mCurrentTrack.value?.id != currentTrackId) {
                viewModelScope.launch {
                    getTrackById(currentTrackId)?.let { trackDomain ->
                        ShowLog.i("$simpleName.mediaControllerCallback.onPlaybackStateChanged,track changed to ${ trackDomain.id }", tagMainViewModel)

                        mCurrentTrack.value = mapper.from(trackDomain)
                        setPosition(0)
                        setIsPlaying(playback.state)
                    }
                }

                return
            }

            setPosition(playback.position)
            setIsPlaying(playback.state)
        }
    }

    private fun updateQueue(queueData: QueueData) {
        val bundle = Bundle()
        bundle.putParcelable(PlayerNavigator.keyQueueData, queueData)

        playerControls.sendCustomAction(MusicService.actionUpdateQueue, bundle)
    }

    private fun setIsPlaying(playbackState: Int) {
        val isPlaying = playbackState == PlaybackStateCompat.STATE_PLAYING
        if (mIsPlaying.value != isPlaying) {
            ShowLog.i("$simpleName.setIsPlaying, isPlaying changed to $isPlaying", tagMainViewModel)
            mIsPlaying.value = isPlaying
        }
    }

    private fun setPosition(newPosition: Long) {
        if (mCurrentPosition.value != newPosition) {
            mCurrentPosition.value = newPosition
        }
    }

    private fun checkQueueTheSame(newQueueData: QueueData): Boolean {
        return newQueueData.type == currentQueueData.type &&
                newQueueData.id == currentQueueData.id
    }
}