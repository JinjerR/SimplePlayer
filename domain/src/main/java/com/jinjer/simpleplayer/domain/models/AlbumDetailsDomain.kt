package com.jinjer.simpleplayer.domain.models

data class AlbumDetailsDomain(
    val id: Int,
    val title: String,
    val trackList: List<TrackDomain>
)