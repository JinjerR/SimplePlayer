package com.jinjer.simpleplayer.domain.usecases

import com.jinjer.simpleplayer.domain.data_sources.IMusicRepository
import com.jinjer.simpleplayer.domain.data_sources.entities.AlbumEntity

class SearchAlbumByTitleUseCase(private val musicRepositoryV2: IMusicRepository) {
    suspend operator fun invoke(query: String): List<AlbumEntity>? =
        musicRepositoryV2.searchAlbumByTitle(query)
}