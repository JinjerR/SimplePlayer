package com.jinjer.simpleplayer.domain.usecases

import com.jinjer.simpleplayer.domain.data_sources.IMusicRepository
import com.jinjer.simpleplayer.domain.data_sources.entities.SingerEntity

class SearchSingerByNameUseCase(private val musicRepository: IMusicRepository) {
    suspend operator fun invoke(query: String): List<SingerEntity>? =
        musicRepository.searchSingerByName(query)
}