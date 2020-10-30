package com.jinjer.simpleplayer.data.repositories

import com.jinjer.simpleplayer.domain.models.TrackDomain
import com.jinjer.simpleplayer.domain.data_sources.IMusicRepository
import com.jinjer.simpleplayer.domain.models.AlbumDetailsDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// TODO: maybe need to make a hashmap instead of a list

class MusicRepository(private val localMusicSource: LocalMusicSource): IMusicRepository {
    private var tracks: List<TrackDomain>? = null

    override suspend fun getTracks(): List<TrackDomain> {
        if (tracks == null) {
            tracks = withContext(Dispatchers.IO) {
                localMusicSource.getTracks()
            }
        }

        return tracks!!
    }

    override suspend fun getAlbumDetailsById(albumId: Int): AlbumDetailsDomain {
        val title = tracks?.find { it.albumId == albumId }?.album ?: ""
        val tracks = tracks?.filter { it.albumId == albumId } ?: emptyList()

        return AlbumDetailsDomain(albumId, title, tracks)
    }

    override suspend fun getTrackById(trackId: Int): TrackDomain? {
        return tracks?.firstOrNull { it.id == trackId }
    }
}