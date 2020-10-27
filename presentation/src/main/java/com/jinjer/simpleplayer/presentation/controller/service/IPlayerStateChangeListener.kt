package com.jinjer.simpleplayer.presentation.controller.service

import com.jinjer.simpleplayer.presentation.controller.service.PlayerState

interface IPlayerStateChangeListener {
    fun onPlayerStateChanged(playerState: PlayerState)
    fun onTrackPositionChanged(newPosition: Int)
}