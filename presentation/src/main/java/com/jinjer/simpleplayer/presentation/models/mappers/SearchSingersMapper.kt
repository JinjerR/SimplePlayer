package com.jinjer.simpleplayer.presentation.models.mappers

import com.jinjer.simpleplayer.domain.models.TrackDomain
import com.jinjer.simpleplayer.domain.utils.Mapper
import com.jinjer.simpleplayer.presentation.main.search.singers.SearchSingerPresenter

class SearchSingersMapper: Mapper<TrackDomain, SearchSingerPresenter>() {
    override fun from(element: TrackDomain) =
        SearchSingerPresenter(
            element.artistId,
            element.artist ?: "",
            0)

    override fun fromList(list: List<TrackDomain>): List<SearchSingerPresenter> {
        return list.distinctBy { it.artistId }
            .map { track ->
                SearchSingerPresenter(
                    track.artistId,
                    track.artist ?: "",
                    list.count { it.artistId == track.artistId }
                )
            }
    }
}