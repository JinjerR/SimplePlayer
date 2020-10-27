package com.jinjer.simpleplayer.presentation.controller.main

import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jinjer.simpleplayer.domain.usecases.GetCurrentTrackIdUseCase
import com.jinjer.simpleplayer.domain.usecases.GetTracksUseCase
import com.jinjer.simpleplayer.presentation.base.SimplePlayerApp
import com.jinjer.simpleplayer.presentation.controller.client.IClientCallback
import com.jinjer.simpleplayer.presentation.utils.SpConstants
import com.jinjer.simpleplayer.presentation.controller.client.MediaClientManager
import com.jinjer.simpleplayer.presentation.controller.service.MusicService
import com.jinjer.simpleplayer.presentation.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    app: SimplePlayerApp,
    private val getTracks: GetTracksUseCase,
    private val getCurrentTrackId: GetCurrentTrackIdUseCase): AndroidViewModel(app), IClientCallback,
    IPlayerController {

    private val appContext = getApplication<SimplePlayerApp>().applicationContext
    private val mediaClientManager = MediaClientManager(appContext, this)

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

    override fun onTokenReceived(token: MediaSessionCompat.Token) {
        mediaController = MediaControllerCompat(appContext, token)
        registerMediaController()

        getCurrentTrackId().let { id ->
            playerControls.prepareFromMediaId(id.toString(), null)
        }
    }

    override fun onCleared() {
        unregisterMediaController()
        mediaClientManager.unbind()
        mediaClientManager.stopService()
    }

    override fun play(trackId: Int) {
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
            val wasLoaded = withContext(Dispatchers.Default) {
                getTracks()
                true
            }

            if (wasLoaded) {
                mediaClientManager.startService()
                mediaClientManager.bind()
                mIsTracksLoaded.value = true
            }
        }
    }

    fun onAppResumes() {
        registerMediaController()
        mediaClientManager.bind()
        mediaClientManager.sendMessageToService(MusicService.actionAppResumes)
    }

    fun onAppStopped() {
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
            val currentTrack: Track = playback.extras?.getParcelable(SpConstants.keyCurrentTrack) ?: return

            if (mCurrentTrack.value?.id != currentTrack.id) {
                mCurrentTrack.value = currentTrack
                mCurrentPosition.value = 0

                return
            }

            val position = playback.position
            if (mCurrentPosition.value != position) {
                mCurrentPosition.value = position
            }

            val state = playbackState.state

            if (state == PlaybackStateCompat.STATE_PLAYING || state == PlaybackStateCompat.STATE_PAUSED) {
                val isPlaying = state == PlaybackStateCompat.STATE_PLAYING
                if (mIsPlaying.value != isPlaying) {
                    mIsPlaying.value = isPlaying
                }
            }
        }
    }
}