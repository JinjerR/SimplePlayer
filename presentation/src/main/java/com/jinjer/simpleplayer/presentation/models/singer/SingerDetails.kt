package com.jinjer.simpleplayer.presentation.models.singer

import com.jinjer.simpleplayer.presentation.models.album.Album
import com.jinjer.simpleplayer.presentation.models.track.Track

data class SingerDetails(
    val singerId: Int,
    val singerName: String,
    val trackList: List<Track>,
    val tracksCount: Int,
    val albumList: List<Album>,
    val albumsCount: Int)