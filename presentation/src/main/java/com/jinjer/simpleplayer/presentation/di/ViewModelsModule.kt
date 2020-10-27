package com.jinjer.simpleplayer.presentation.di

import com.jinjer.simpleplayer.domain.utils.DIConstants
import com.jinjer.simpleplayer.domain.utils.DIConstants.tagClientController
import com.jinjer.simpleplayer.domain.utils.DIConstants.tagPlayerController
import com.jinjer.simpleplayer.presentation.base.MainActivityViewModel
import com.jinjer.simpleplayer.presentation.main.player.CollapsedPlayerViewModel
import com.jinjer.simpleplayer.presentation.main.player.PlayerFragmentViewModel
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
    bindViewModel<PlayerFragmentViewModel>() with provider { PlayerFragmentViewModel(instance(tagPlayerController)) }
    bindViewModel<CollapsedPlayerViewModel>() with provider { CollapsedPlayerViewModel(instance(tagPlayerController)) }
    bindViewModel<TracksViewModel>() with provider { TracksViewModel(instance(tagClientController), instance(), TrackPresenterMapper()) }
    bindViewModel<SearchTracksViewModel>() with provider { SearchTracksViewModel(instance(tagPlayerController), instance(), SearchTracksMapper()) }
    bindViewModel<SearchSingersViewModel>() with provider { SearchSingersViewModel(instance(tagPlayerController), instance(), SearchSingersMapper()) }
    bindViewModel<SearchAlbumsViewModel>() with provider { SearchAlbumsViewModel(instance(tagPlayerController), instance(), SearchAlbumsMapper()) }
}

val activityViewModelsModule = Module(DIConstants.moduleActivityViewModels) {
    bindViewModel<MainActivityViewModel>() with provider { MainActivityViewModel(instance(tagClientController)) }
}