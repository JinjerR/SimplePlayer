package com.jinjer.simpleplayer.presentation.main.search.albums

import com.jinjer.simpleplayer.domain.usecases.SearchAlbumByTitleUseCase
import com.jinjer.simpleplayer.presentation.models.album.Album
import com.jinjer.simpleplayer.presentation.main.search.base.SearchViewModelBase
import com.jinjer.simpleplayer.presentation.models.album.AlbumMapper

class SearchAlbumsViewModel(
    private val searchAlbums: SearchAlbumByTitleUseCase,
    private val mapper: AlbumMapper
): SearchViewModelBase<Album>() {

    override suspend fun prepareSearch(query: String) {
        searchAlbums(query)?.let {
            mSearchResult.value = mapper.fromList(it)
        }
    }
}