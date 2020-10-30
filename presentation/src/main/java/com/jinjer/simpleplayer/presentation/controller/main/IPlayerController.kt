package com.jinjer.simpleplayer.presentation.controller.main

import androidx.lifecycle.LiveData
import com.jinjer.simpleplayer.presentation.controller.service.QueueData
import com.jinjer.simpleplayer.presentation.models.Track

interface IPlayerController {
    fun play(trackId: Int, playbackQueue: QueueData)

    fun resume()

    fun seekTo(position: Int)

    fun skipToNext()

    fun skipToPrevious()

    val currentTrack: LiveData<Track>

    val currentPosition: LiveData<Long>

    val isPlaying: LiveData<Boolean>

    val isTracksLoaded: LiveData<Boolean>
}