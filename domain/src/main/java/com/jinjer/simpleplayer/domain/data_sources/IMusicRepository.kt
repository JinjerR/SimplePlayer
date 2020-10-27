package com.jinjer.simpleplayer.domain.data_sources

import com.jinjer.simpleplayer.domain.models.TrackDomain

interface IMusicRepository {
    suspend fun getTracks(): List<TrackDomain>
}