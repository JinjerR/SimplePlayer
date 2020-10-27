package com.jinjer.simpleplayer.data.repositories

import com.jinjer.simpleplayer.domain.models.TrackDomain

interface IMusicSource {
    suspend fun getTracks(): List<TrackDomain>
}