package com.jinjer.simpleplayer.presentation.controller.service

import com.jinjer.simpleplayer.presentation.models.Track

interface IPlayerNavigator {
    fun currentTrack(): Int?

    fun nextTrack(): Int?

    fun previousTrack(): Int?

    fun setTrack(trackId: Int): Boolean

    fun setQueue(queueData: QueueData, initialTrackId: Int? = null)

    fun setFirstTrackIfExist()
}