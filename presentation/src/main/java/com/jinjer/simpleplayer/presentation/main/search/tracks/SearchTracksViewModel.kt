package com.jinjer.simpleplayer.presentation.main.search.tracks

import com.jinjer.simpleplayer.domain.usecases.SearchTrackByTitleUseCase
import com.jinjer.simpleplayer.presentation.main.search.base.SearchViewModelBase
import com.jinjer.simpleplayer.presentation.models.track.Track
import com.jinjer.simpleplayer.presentation.models.track.TrackMapper

class SearchTracksViewModel(
    private val searchTracks: SearchTrackByTitleUseCase,
    private val mapper: TrackMapper
): SearchViewModelBase<Track>() {

    override suspend fun prepareSearch(query: String) {
        searchTracks(query)?.let {
            mSearchResult.value = mapper.fromList(it)
        }
    }
}