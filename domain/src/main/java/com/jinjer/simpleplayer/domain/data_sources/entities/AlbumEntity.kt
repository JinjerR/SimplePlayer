package com.jinjer.simpleplayer.domain.data_sources.entities

data class AlbumEntity(
    val albumId: Int,
    val albumName: String?,
    val tracks: List<TrackEntity>?)