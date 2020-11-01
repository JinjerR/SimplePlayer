package com.jinjer.simpleplayer.domain.usecases

import com.jinjer.simpleplayer.domain.data_sources.IMusicRepository
import com.jinjer.simpleplayer.domain.data_sources.entities.AlbumEntity

class GetAlbumByIdUseCase(private val musicRepository: IMusicRepository) {
    suspend operator fun invoke(albumId: Int): AlbumEntity? =
        musicRepository.getAlbumById(albumId)
}