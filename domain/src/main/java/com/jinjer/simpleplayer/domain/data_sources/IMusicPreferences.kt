package com.jinjer.simpleplayer.domain.data_sources

interface IMusicPreferences {
    fun setCurrentTrackId(trackId: Int)
    fun getCurrentTrackId(): Int
}