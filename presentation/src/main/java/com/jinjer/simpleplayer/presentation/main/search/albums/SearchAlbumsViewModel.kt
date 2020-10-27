package com.jinjer.simpleplayer.presentation.main.search.albums

import com.jinjer.simpleplayer.domain.usecases.GetTracksUseCase
import com.jinjer.simpleplayer.presentation.main.search.base.SearchViewModelBase
import com.jinjer.simpleplayer.presentation.models.mappers.SearchAlbumsMapper

class SearchAlbumsViewModel(
    getTracks: GetTracksUseCase,
    mapper: SearchAlbumsMapper): SearchViewModelBase<SearchAlbumPresenter>(getTracks, mapper) {

    override fun filter(element: SearchAlbumPresenter): Boolean {
        return element.albumName.contains(query, true)
    }
}