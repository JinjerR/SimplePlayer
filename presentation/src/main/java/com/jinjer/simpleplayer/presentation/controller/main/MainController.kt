package com.jinjer.simpleplayer.presentation.controller.main

import android.content.Context
import android.os.Handler
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.media.session.PlaybackStateCompat.STATE_PAUSED
import android.support.v4.media.session.PlaybackStateCompat.STATE_PLAYING
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.jinjer.simpleplayer.presentation.models.Track
import com.jinjer.simpleplayer.presentation.controller.ActionPlayback
import com.jinjer.simpleplayer.presentation.controller.ActionPlayback.actionEmpty
import com.jinjer.simpleplayer.presentation.controller.ActionPlayback.actionPlayPause
import com.jinjer.simpleplayer.presentation.controller.ActionPlayback.actionPositionChanged
import com.jinjer.simpleplayer.presentation.controller.ActionPlayback.actionStop
import com.jinjer.simpleplayer.presentation.controller.ActionPlayback.actionTrackChanged
import com.jinjer.simpleplayer.presentation.controller.ActionPlayback.keyCurrentTrack
import com.jinjer.simpleplayer.presentation.controller.ActionPlayback.keyPlaybackAction
import com.jinjer.simpleplayer.presentation.controller.client.MediaClientManager
import com.jinjer.simpleplayer.presentation.controller.client.IClientManager
import com.jinjer.simpleplayer.presentation.controller.service.MusicService
import com.jinjer.simpleplayer.presentation.controller.service.MusicService.Companion.tagMusicControl
import com.jinjer.simpleplayer.presentation.utils.ShowLog
import com.jinjer.simpleplayer.presentation.utils.ShowLog.tagTest

class MainController(private val context: Context): IClientController, IClientCallback {

    private val simpleName = MainController::class.java.simpleName

    private val mediaClientManager: IClientManager = MediaClientManager(context, this)

    private val playerControls: MediaControllerCompat.TransportControls
        get() = mediaController.transportControls

    private val mCurrentTrack = MutableLiveData( Track.emptyTrack() )
    override val currentTrack: LiveData<Track> = mCurrentTrack

    private val mCurrentPosition = MutableLiveData(-1L)
    override val currentPosition: LiveData<Long> = mCurrentPosition

    private val mIsPlaying = MutableLiveData(false)
    override val isPlaying: LiveData<Boolean> = mIsPlaying

    private val mIsTracksLoaded = MutableLiveData(false)
    override val isTracksLoaded: LiveData<Boolean> = mIsTracksLoaded

    private lateinit var mediaController: MediaControllerCompat

    private val observer: Observer<Boolean> = Observer {
        ShowLog.i("isPlaying = $it", tagTest)
    }

    override fun connect() {
        mIsPlaying.observeForever(observer)
        mediaClientManager.connect()
    }

    override fun disconnect() {
        mediaClientManager.disconnect()
        mIsPlaying.removeObserver(observer)
    }

    override fun sendEmptyMessage(action: Int) {
        mediaClientManager.sendEmptyMessage(action)
    }

    override fun play(trackId: Int) {
        if (trackId == mCurrentTrack.value?.id) {
            if (mIsPlaying.value == false) {
                playerControls.play()
            } else {
                playerControls.pause()
            }

            return
        }

        playerControls.playFromMediaId(trackId.toString(), null)
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

    override fun onConnectedToService(sessionToken: MediaSessionCompat.Token) {
        ShowLog.i("$simpleName.onConnectedToService()", tagMusicControl)

        mediaController = MediaControllerCompat(context, sessionToken)
        mediaController.registerCallback(mediaControllerCallback)

        mediaClientManager.sendEmptyMessage(MusicService.actionInitialTrack)
    }

    override fun onTracksLoaded() {
        mIsTracksLoaded.value = true
    }

    private val mediaControllerCallback = object: MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged(playbackState: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(playbackState)

            val playback = playbackState ?: return
            val currentTrack: Track = playback.extras?.getParcelable(keyCurrentTrack) ?: return

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

            if (state == STATE_PLAYING || state == STATE_PAUSED) {
                val isPlaying = state == STATE_PLAYING
                if (mIsPlaying.value != isPlaying) {
//                    ShowLog.i("$simpleName.onPlaybackStateChanged, isPlaying changed, newValue = $isPlaying", tagTest)
                    mIsPlaying.value = isPlaying
                }
            }
        }
    }
}