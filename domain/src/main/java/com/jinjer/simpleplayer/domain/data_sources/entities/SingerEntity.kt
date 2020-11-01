package com.jinjer.simpleplayer.domain.data_sources.entities

data class SingerEntity(
    val singerId: Int,
    val singerName: String?,
    val albums: List<AlbumEntity>?)