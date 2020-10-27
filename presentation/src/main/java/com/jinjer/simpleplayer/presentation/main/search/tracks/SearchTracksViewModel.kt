package com.jinjer.simpleplayer.presentation.main.search.tracks

import androidx.lifecycle.LiveData
import com.jinjer.simpleplayer.domain.usecases.GetTracksUseCase
import com.jinjer.simpleplayer.presentation.controller.main.IPlayerController
import com.jinjer.simpleplayer.presentation.main.search.base.SearchViewModelBase
import com.jinjer.simpleplayer.presentation.main.tracks.TrackPresenter
import com.jinjer.simpleplayer.presentation.models.mappers.SearchTracksMapper

class SearchTracksViewModel(
    private val playerController: IPlayerController,
    getTracks: GetTracksUseCase,
    mapper: SearchTracksMapper
): SearchViewModelBase<TrackPresenter>(playerController, getTracks, mapper) {

//    val isPlaying: LiveData<Boolean> = playerController.isPlaying

    override fun filter(element: TrackPresenter): Boolean {
        return element.trackName.contains(query, true)
    }

    fun playTrack(trackId: Int) {
        playerController.play(trackId)
    }
}