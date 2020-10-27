package com.jinjer.simpleplayer.presentation.models.mappers

import com.jinjer.simpleplayer.domain.utils.Mapper
import com.jinjer.simpleplayer.domain.models.TrackDomain
import com.jinjer.simpleplayer.presentation.models.Track

class TrackMapper : Mapper<TrackDomain, Track>() {
    override fun from(element: TrackDomain): Track {
        return Track(
            element.id,
            element.title ?: "",
            element.artist ?: "",
            element.artistId,
            element.album ?: "",
            element.albumId,
            element.duration ?: -1L
        )
    }
}