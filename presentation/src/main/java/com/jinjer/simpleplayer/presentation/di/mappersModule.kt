package com.jinjer.simpleplayer.presentation.di

import com.jinjer.simpleplayer.domain.utils.DIConstants
import com.jinjer.simpleplayer.presentation.models.album.AlbumDetailsMapper
import com.jinjer.simpleplayer.presentation.models.album.AlbumMapper
import com.jinjer.simpleplayer.presentation.models.singer.SingerDetailsMapper
import com.jinjer.simpleplayer.presentation.models.singer.SingerMapper
import com.jinjer.simpleplayer.presentation.models.track.TrackDataMapper
import com.jinjer.simpleplayer.presentation.models.track.TrackMapper
import org.kodein.di.DI.Module
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val mappersModule = Module(DIConstants.moduleMappers) {
    bind<TrackDataMapper>() with provider { TrackDataMapper() }
    bind<TrackMapper>() with provider { TrackMapper() }
    bind<SingerDetailsMapper>() with provider { SingerDetailsMapper(instance(), instance()) }
    bind<AlbumDetailsMapper>() with provider { AlbumDetailsMapper(instance()) }
    bind<AlbumMapper>() with provider { AlbumMapper() }
    bind<SingerMapper>() with provider { SingerMapper() }
}