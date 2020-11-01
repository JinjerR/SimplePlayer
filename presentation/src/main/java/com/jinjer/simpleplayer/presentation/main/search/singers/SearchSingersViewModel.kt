package com.jinjer.simpleplayer.presentation.main.search.singers

import com.jinjer.simpleplayer.domain.usecases.SearchSingerByNameUseCase
import com.jinjer.simpleplayer.presentation.main.search.base.SearchViewModelBase
import com.jinjer.simpleplayer.presentation.models.singer.Singer
import com.jinjer.simpleplayer.presentation.models.singer.SingerMapper

class SearchSingersViewModel(
    private val searchSingers: SearchSingerByNameUseCase,
    private val singerMapper: SingerMapper
): SearchViewModelBase<Singer>() {

    override suspend fun prepareSearch(query: String) {
        searchSingers(query)?.let {
            mSearchResult.value = singerMapper.fromList(it)
        }
    }
}