package com.jinjer.simpleplayer.presentation.di

import com.jinjer.simpleplayer.domain.usecases.*
import com.jinjer.simpleplayer.domain.utils.DIConstants
import org.kodein.di.DI.Module
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val useCasesModule = Module(DIConstants.moduleUseCases) {
    bind<GetTracksUseCase>() with provider { GetTracksUseCase(instance()) }

    bind<GetCurrentTrackIdUseCase>() with provider { GetCurrentTrackIdUseCase(instance()) }
    bind<SetCurrentTrackIdUseCase>() with provider { SetCurrentTrackIdUseCase(instance()) }

    bind<GetTrackByIdUseCase>() with provider { GetTrackByIdUseCase(instance()) }
    bind<GetAlbumByIdUseCase>() with provider { GetAlbumByIdUseCase(instance()) }
    bind<GetSingerByIdUseCase>() with provider { GetSingerByIdUseCase(instance()) }

    bind<SearchAlbumByTitleUseCase>() with provider { SearchAlbumByTitleUseCase(instance()) }
    bind<SearchSingerByNameUseCase>() with provider { SearchSingerByNameUseCase(instance()) }
    bind<SearchTrackByTitleUseCase>() with provider { SearchTrackByTitleUseCase(instance()) }
}