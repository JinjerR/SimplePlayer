package com.jinjer.simpleplayer.presentation.controller.service

interface IPlayer {
    fun prepareForPlaying(trackId: Int)

    fun play(trackId: Int)

    fun resume()

    fun pause()

    fun stop()

    fun seekTo(position: Int)

    fun onAppEventHappened(event: AppEvent)

    fun getCurrentlyPlayingTrackId(): Int

    val isPlaying: Boolean

    val currentPosition: Int
}