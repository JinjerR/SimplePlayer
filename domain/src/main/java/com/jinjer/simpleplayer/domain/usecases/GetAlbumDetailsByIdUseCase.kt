package com.jinjer.simpleplayer.domain.usecases

import com.jinjer.simpleplayer.domain.data_sources.IMusicRepository
import com.jinjer.simpleplayer.domain.models.AlbumDetailsDomain

class GetAlbumDetailsByIdUseCase(private val musicRepository: IMusicRepository) {
    suspend operator fun invoke(albumId: Int): AlbumDetailsDomain =
        musicRepository.getAlbumDetailsById(albumId)
}