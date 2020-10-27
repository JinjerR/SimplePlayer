package com.jinjer.simpleplayer.presentation.main.search.tracks

import com.jinjer.simpleplayer.domain.usecases.GetTracksUseCase
import com.jinjer.simpleplayer.presentation.main.search.base.SearchViewModelBase
import com.jinjer.simpleplayer.presentation.main.tracks.TrackPresenter
import com.jinjer.simpleplayer.presentation.models.mappers.SearchTracksMapper

class SearchTracksViewModel(
    getTracks: GetTracksUseCase,
    mapper: SearchTracksMapper
): SearchViewModelBase<TrackPresenter>(getTracks, mapper) {

    override fun filter(element: TrackPresenter): Boolean {
        return element.trackName.contains(query, true)
    }
}