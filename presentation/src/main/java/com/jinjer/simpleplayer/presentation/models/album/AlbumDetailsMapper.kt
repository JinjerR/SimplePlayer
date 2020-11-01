package com.jinjer.simpleplayer.presentation.models.album

import com.jinjer.simpleplayer.domain.data_sources.entities.AlbumEntity
import com.jinjer.simpleplayer.domain.utils.Mapper
import com.jinjer.simpleplayer.presentation.models.track.TrackMapper
import com.jinjer.simpleplayer.presentation.utils.Utils

class AlbumDetailsMapper(private val trackMapper: TrackMapper
): Mapper<AlbumEntity, AlbumDetails>() {

    override suspend fun from(element: AlbumEntity): AlbumDetails {
        return with(element) {
            AlbumDetails(
                id = albumId,
                title = albumName ?: "",
                trackList = trackMapper.fromList(tracks ?: emptyList()),
                tracksNumber = tracks?.size ?: 0,
                albumArtUri = Utils.getAlbumArtUri(albumId.toLong())
            )
        }
    }
}