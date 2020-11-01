package com.jinjer.simpleplayer.data.repositories

import com.jinjer.simpleplayer.domain.data_sources.IMusicRepository
import com.jinjer.simpleplayer.domain.data_sources.entities.AlbumEntity
import com.jinjer.simpleplayer.domain.data_sources.entities.SingerEntity
import com.jinjer.simpleplayer.domain.data_sources.entities.TrackEntity

class MusicRepository(private val localMusicSource: LocalMusicSource): IMusicRepository {

    override suspend fun getTrackById(trackId: Int): TrackEntity? {
        return localMusicSource.getTrackById(trackId)
    }

    override suspend fun getAlbumById(albumId: Int): AlbumEntity? {
        return localMusicSource.getAlbumById(albumId)
    }

    override suspend fun getSingerById(singerId: Int): SingerEntity? {
        return localMusicSource.getSingerById(singerId)
    }

    override suspend fun searchSingerByName(query: String): List<SingerEntity>? {
        return localMusicSource.searchSingerByName(query)
    }

    override suspend fun searchAlbumByTitle(query: String): List<AlbumEntity>? {
        return localMusicSource.searchAlbumByTitle(query)
    }

    override suspend fun searchTrackByTitle(query: String): List<TrackEntity> {
        return localMusicSource.searchTrackByTitle(query)
    }

    override suspend fun loadTracks(): List<TrackEntity>? {
        return localMusicSource.loadTracks()
    }
}