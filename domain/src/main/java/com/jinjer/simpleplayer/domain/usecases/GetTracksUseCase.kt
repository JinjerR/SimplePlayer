package com.jinjer.simpleplayer.domain.usecases

import com.jinjer.simpleplayer.domain.models.TrackDomain
import com.jinjer.simpleplayer.domain.data_sources.IMusicRepository

class GetTracksUseCase(private val musicRepository: IMusicRepository) {
    suspend operator fun invoke(): List<TrackDomain> =
        musicRepository.getTracks()
}