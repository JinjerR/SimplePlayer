package com.jinjer.simpleplayer.presentation.models.track

import com.jinjer.simpleplayer.domain.utils.Mapper
import com.jinjer.simpleplayer.domain.data_sources.entities.TrackEntity

class TrackMapper: Mapper<TrackEntity, Track>() {
    override suspend fun from(element: TrackEntity): Track {
        return Track(
            element.id,
            element.title ?: "",
            element.artist ?: ""
        )
    }
}