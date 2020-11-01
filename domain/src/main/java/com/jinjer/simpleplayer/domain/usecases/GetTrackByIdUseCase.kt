package com.jinjer.simpleplayer.domain.usecases

import com.jinjer.simpleplayer.domain.data_sources.IMusicRepository
import com.jinjer.simpleplayer.domain.data_sources.entities.TrackEntity

class GetTrackByIdUseCase(private val musicRepository: IMusicRepository) {
    suspend operator fun invoke(trackId: Int): TrackEntity? =
        musicRepository.getTrackById(trackId)
}