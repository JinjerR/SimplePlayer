package com.jinjer.simpleplayer.domain.usecases

import com.jinjer.simpleplayer.domain.data_sources.IMusicRepository
import com.jinjer.simpleplayer.domain.data_sources.entities.TrackEntity

class GetTracksUseCase(private val musicRepository: IMusicRepository) {
    suspend operator fun invoke(): List<TrackEntity>? =
        musicRepository.loadTracks()
}