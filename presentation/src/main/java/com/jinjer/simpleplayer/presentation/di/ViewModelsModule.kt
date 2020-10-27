package com.jinjer.simpleplayer.presentation.di

import com.jinjer.simpleplayer.domain.utils.DIConstants
import com.jinjer.simpleplayer.presentation.controller.main.MainViewModel
import com.jinjer.simpleplayer.presentation.main.search.albums.SearchAlbumsViewModel
import com.jinjer.simpleplayer.presentation.main.search.singers.SearchSingersViewModel
import com.jinjer.simpleplayer.presentation.main.search.tracks.SearchTracksViewModel
import com.jinjer.simpleplayer.presentation.main.tracks.TracksViewModel
import com.jinjer.simpleplayer.presentation.models.mappers.SearchAlbumsMapper
import com.jinjer.simpleplayer.presentation.models.mappers.SearchSingersMapper
import com.jinjer.simpleplayer.presentation.models.mappers.SearchTracksMapper
import com.jinjer.simpleplayer.presentation.models.mappers.TrackPresenterMapper
import com.jinjer.simpleplayer.presentation.utils.extensions.bindViewModel
import org.kodein.di.*
import org.kodein.di.DI.Module

val fragmentViewModelsModule = Module(DIConstants.moduleFragmentViewModels) {
    bindViewModel<TracksViewModel>() with provider { TracksViewModel(instance(), TrackPresenterMapper()) }
    bindViewModel<SearchTracksViewModel>() with provider { SearchTracksViewModel(instance(), SearchTracksMapper()) }
    bindViewModel<SearchSingersViewModel>() with provider { SearchSingersViewModel(instance(), SearchSingersMapper()) }
    bindViewModel<SearchAlbumsViewModel>() with provider { SearchAlbumsViewModel(instance(), SearchAlbumsMapper()) }
}

val activityViewModelsModule = Module(DIConstants.moduleActivityViewModels) {
    bindViewModel<MainViewModel>() with provider { MainViewModel(instance("application"), instance(), instance()) }
}