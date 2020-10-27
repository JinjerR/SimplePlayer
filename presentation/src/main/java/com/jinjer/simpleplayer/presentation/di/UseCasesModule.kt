package com.jinjer.simpleplayer.presentation.di

import com.jinjer.simpleplayer.domain.usecases.GetCurrentTrackIdUseCase
import com.jinjer.simpleplayer.domain.usecases.GetTracksUseCase
import com.jinjer.simpleplayer.domain.usecases.SetCurrentTrackIdUseCase
import com.jinjer.simpleplayer.domain.utils.DIConstants
import org.kodein.di.DI.Module
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val useCasesModule = Module(DIConstants.moduleUseCases) {
    bind<GetTracksUseCase>() with provider { GetTracksUseCase(instance()) }
    bind<GetCurrentTrackIdUseCase>() with provider { GetCurrentTrackIdUseCase(instance()) }
    bind<SetCurrentTrackIdUseCase>() with provider { SetCurrentTrackIdUseCase(instance()) }
}