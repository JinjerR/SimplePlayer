package com.jinjer.simpleplayer.presentation.models.singer

import com.jinjer.simpleplayer.domain.data_sources.entities.SingerEntity
import com.jinjer.simpleplayer.domain.utils.Mapper

class SingerMapper: Mapper<SingerEntity, Singer>() {

    override suspend fun from(element: SingerEntity): Singer {
        val name = element.singerName ?: ""
        var trackCount = 0

        element.albums?.map { album ->
            album.tracks?.let { tracks ->
                trackCount += tracks.size
            }
        }

        return Singer(
            element.singerId,
            name,
            trackCount
        )
    }
}