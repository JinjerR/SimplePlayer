package com.jinjer.simpleplayer.domain.usecases

import com.jinjer.simpleplayer.domain.data_sources.IMusicRepository
import com.jinjer.simpleplayer.domain.data_sources.entities.SingerEntity

class GetSingerByIdUseCase(private val musicRepository: IMusicRepository) {
    suspend operator fun invoke(singerId: Int): SingerEntity? =
        musicRepository.getSingerById(singerId)
}