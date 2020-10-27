package com.jinjer.simpleplayer.data.di

import com.jinjer.simpleplayer.data.preferencies.MusicPreferences
import com.jinjer.simpleplayer.data.repositories.LocalMusicSource
import com.jinjer.simpleplayer.data.repositories.MusicRepository
import com.jinjer.simpleplayer.domain.data_sources.IMusicPreferences
import com.jinjer.simpleplayer.domain.data_sources.IMusicRepository
import com.jinjer.simpleplayer.domain.utils.DIConstants
import org.kodein.di.DI.Module
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

val dataSourcesModule = Module(DIConstants.moduleDataSources) {
    bind<LocalMusicSource>() with provider { LocalMusicSource(instance()) }
    bind<IMusicRepository>() with singleton { MusicRepository(instance()) }
    bind<IMusicPreferences>() with singleton { MusicPreferences(instance()) }
}