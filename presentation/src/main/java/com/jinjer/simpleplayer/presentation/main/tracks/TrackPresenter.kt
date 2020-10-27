package com.jinjer.simpleplayer.presentation.main.tracks

data class TrackPresenter(
    val trackId: Int,
    val trackName: String,
    val bandName: String,
    var isSelected: Boolean = false,
    var isPlaying: Boolean = false)