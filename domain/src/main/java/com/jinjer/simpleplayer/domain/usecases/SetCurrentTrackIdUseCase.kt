package com.jinjer.simpleplayer.domain.usecases

import com.jinjer.simpleplayer.domain.data_sources.IMusicPreferences

class SetCurrentTrackIdUseCase(private val preferences: IMusicPreferences) {
    operator fun invoke(trackId: Int) {
        preferences.setCurrentTrackId(trackId)
    }
}