package com.jinjer.simpleplayer.data.repositories

import com.jinjer.simpleplayer.domain.models.TrackDomain
import com.jinjer.simpleplayer.domain.data_sources.IMusicRepository

class MusicRepository(private val localMusicSource: LocalMusicSource): IMusicRepository {
    private var tracks: List<TrackDomain>? = null

    override suspend fun getTracks(): List<TrackDomain> {
        return tracks ?: run {
            tracks = localMusicSource.getTracks()
            tracks!!
        }
    }
}