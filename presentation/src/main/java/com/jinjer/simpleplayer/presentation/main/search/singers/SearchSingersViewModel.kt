package com.jinjer.simpleplayer.presentation.main.search.singers

import com.jinjer.simpleplayer.domain.usecases.GetTracksUseCase
import com.jinjer.simpleplayer.presentation.main.search.base.SearchViewModelBase
import com.jinjer.simpleplayer.presentation.models.mappers.SearchSingersMapper

class SearchSingersViewModel(
    getTracks: GetTracksUseCase,
    mapper: SearchSingersMapper
): SearchViewModelBase<SearchSingerPresenter>(getTracks, mapper) {

    override fun filter(element: SearchSingerPresenter): Boolean {
        return element.bandName.contains(query, true)
    }
}