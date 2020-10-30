package com.jinjer.simpleplayer.presentation.di

import com.jinjer.simpleplayer.domain.utils.DIConstants
import com.jinjer.simpleplayer.presentation.controller.main.MainViewModel
import com.jinjer.simpleplayer.presentation.main.albums.AlbumDetailsContentViewModel
import com.jinjer.simpleplayer.presentation.main.search.albums.SearchAlbumsViewModel
import com.jinjer.simpleplayer.presentation.main.search.singers.SearchSingersViewModel
import com.jinjer.simpleplayer.presentation.main.search.tracks.SearchTracksViewModel
import com.jinjer.simpleplayer.presentation.main.tracks.TracksViewModel
import com.jinjer.simpleplayer.presentation.models.mappers.*
import com.jinjer.simpleplayer.presentation.utils.extensions.bindViewModel
import org.kodein.di.*
import org.kodein.di.DI.Module

val viewModelsModule = Module(DIConstants.moduleFragmentViewModels) {
    bindViewModel<TracksViewModel>() with provider { TracksViewModel(instance(), TrackPresenterMapper()) }
    bindViewModel<SearchTracksViewModel>() with provider { SearchTracksViewModel(instance(), SearchTracksMapper()) }
    bindViewModel<SearchSingersViewModel>() with provider { SearchSingersViewModel(instance(), SearchSingersMapper()) }
    bindViewModel<SearchAlbumsViewModel>() with provider { SearchAlbumsViewModel(instance(), SearchAlbumsMapper()) }
    bindViewModel<AlbumDetailsContentViewModel>() with provider { AlbumDetailsContentViewModel(instance(), AlbumDetailsMapper()) }
    bindViewModel<MainViewModel>() with provider { MainViewModel(instance("application"), instance(), instance(), instance(), TrackMapper()) }
}