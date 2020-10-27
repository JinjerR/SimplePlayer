package com.jinjer.simpleplayer.domain.usecases

import com.jinjer.simpleplayer.domain.data_sources.IMusicPreferences

class GetCurrentTrackIdUseCase(private val preferences: IMusicPreferences) {
    operator fun invoke(): Int = preferences.getCurrentTrackId()
}