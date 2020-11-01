package com.jinjer.simpleplayer.presentation.models.track

import com.jinjer.simpleplayer.domain.utils.Mapper
import com.jinjer.simpleplayer.domain.data_sources.entities.TrackEntity

class TrackDataMapper : Mapper<TrackEntity, TrackData>() {
    override suspend fun from(element: TrackEntity): TrackData {
        return TrackData(
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