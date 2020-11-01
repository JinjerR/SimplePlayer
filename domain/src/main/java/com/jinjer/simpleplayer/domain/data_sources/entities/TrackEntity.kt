package com.jinjer.simpleplayer.domain.data_sources.entities

data class TrackEntity (
    val id: Int = -1,
    val title: String? = null,
    val artist: String? = null,
    val artistId: Int,
    val album: String? = null,
    val albumId: Int,
    val duration: Long? = null)