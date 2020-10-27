package com.jinjer.simpleplayer.presentation.models.mappers

import com.jinjer.simpleplayer.domain.models.TrackDomain
import com.jinjer.simpleplayer.domain.utils.Mapper
import com.jinjer.simpleplayer.presentation.main.search.albums.SearchAlbumPresenter
import com.jinjer.simpleplayer.presentation.utils.Utils

class SearchAlbumsMapper: Mapper<TrackDomain, SearchAlbumPresenter>() {
    override fun from(element: TrackDomain): SearchAlbumPresenter =
        SearchAlbumPresenter(
            element.albumId,
            element.album ?: "",
            Utils.getAlbumArtUri(element.albumId.toLong())
        )

    override fun fromList(list: List<TrackDomain>): List<SearchAlbumPresenter> {
        return list.distinctBy { it.albumId }
            .map { from(it) }
    }
}