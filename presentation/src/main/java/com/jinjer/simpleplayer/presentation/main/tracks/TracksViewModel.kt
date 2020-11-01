package com.jinjer.simpleplayer.presentation.main.tracks

import androidx.lifecycle.*
import com.jinjer.simpleplayer.domain.usecases.GetTracksUseCase
import com.jinjer.simpleplayer.presentation.models.track.TrackMapper
import com.jinjer.simpleplayer.presentation.models.track.Track
import kotlinx.coroutines.launch

class TracksViewModel(
    private val getTracks: GetTracksUseCase,
    private val mapper: TrackMapper
): ViewModel() {

    private val mTracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> = mTracks

    fun onTracksLoaded(tracks: List<Track>?) {
        viewModelScope.launch {
            tracks?.let {
                mTracks.value = it
                return@launch
            }

            getTracks()?.let {
                mTracks.value = mapper.fromList(it)
            }
        }
    }
}