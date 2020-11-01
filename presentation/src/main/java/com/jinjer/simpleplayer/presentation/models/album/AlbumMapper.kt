package com.jinjer.simpleplayer.presentation.models.album

import com.jinjer.simpleplayer.domain.data_sources.entities.AlbumEntity
import com.jinjer.simpleplayer.domain.utils.Mapper
import com.jinjer.simpleplayer.presentation.utils.Utils

class AlbumMapper: Mapper<AlbumEntity, Album>() {
    override suspend fun from(element: AlbumEntity): Album =
        Album(
            element.albumId,
            element.albumName ?: "",
            Utils.getAlbumArtUri(element.albumId.toLong())
        )
}