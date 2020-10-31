package com.jinjer.simpleplayer.presentation.main.tracks

import androidx.lifecycle.*
import com.jinjer.simpleplayer.domain.usecases.GetTracksUseCase
import com.jinjer.simpleplayer.presentation.models.Track
import com.jinjer.simpleplayer.presentation.models.mappers.TrackPresenterMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TracksViewModel(
    private val getTracks: GetTracksUseCase,
    private val mapper: TrackPresenterMapper): ViewModel() {

    private val mTracks = MutableLiveData<List<TrackPresenter>>()
    val tracks: LiveData<List<TrackPresenter>> = mTracks

    fun onTracksLoaded(tracks: List<TrackPresenter>?) {
        viewModelScope.launch {
            mTracks.value = tracks ?: mapper.fromList(getTracks())
        }
    }
}