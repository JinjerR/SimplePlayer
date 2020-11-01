package com.jinjer.simpleplayer.data.repositories

import com.jinjer.simpleplayer.domain.data_sources.entities.AlbumEntity
import com.jinjer.simpleplayer.domain.data_sources.entities.SingerEntity
import com.jinjer.simpleplayer.domain.data_sources.entities.TrackEntity

interface IMusicSource {
    suspend fun loadTracks(): List<TrackEntity>?

    suspend fun getTrackById(trackId: Int): TrackEntity?

    suspend fun getAlbumById(albumId: Int): AlbumEntity?

    suspend fun getSingerById(singerId: Int): SingerEntity?

    suspend fun searchSingerByName(query: String): List<SingerEntity>?

    suspend fun searchAlbumByTitle(query: String): List<AlbumEntity>?

    suspend fun searchTrackByTitle(query: String): List<TrackEntity>?
}