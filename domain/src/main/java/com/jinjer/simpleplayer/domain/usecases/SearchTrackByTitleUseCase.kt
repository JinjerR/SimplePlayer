package com.jinjer.simpleplayer.domain.usecases

import com.jinjer.simpleplayer.domain.data_sources.IMusicRepository
import com.jinjer.simpleplayer.domain.data_sources.entities.TrackEntity

class SearchTrackByTitleUseCase(private val musicRepositoryV2: IMusicRepository) {
    suspend operator fun invoke(query: String): List<TrackEntity>? =
        musicRepositoryV2.searchTrackByTitle(query)
}