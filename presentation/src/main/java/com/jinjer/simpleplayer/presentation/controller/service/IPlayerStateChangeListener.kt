package com.jinjer.simpleplayer.presentation.controller.service

interface IPlayerStateChangeListener {
    fun onPlayerStateChanged(playerState: PlayerState)
    fun onTrackPositionChanged(newPosition: Int)
}