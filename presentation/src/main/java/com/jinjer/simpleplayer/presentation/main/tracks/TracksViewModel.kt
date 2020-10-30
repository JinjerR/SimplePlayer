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

    private val mSelectedTrackIdx = MutableLiveData<Int>()
    val selectedTrackIdx: LiveData<Int> = mSelectedTrackIdx

    fun onTracksLoaded(tracks: List<TrackPresenter>?) {
        viewModelScope.launch {
            mTracks.value = tracks ?: mapper.fromList(getTracks())
        }
    }

    // since the track changes not only from the current list, it is necessary to listen to its changes
    // from other places too (for example, from the track playback window, the "next track" button),
    // the track is converted to its position in the current list in order to transfer this index
    // to the adapter and make it selected.
    fun onTrackChanged(track: Track) {
        viewModelScope.launch {
            val currentIdx = withContext(Dispatchers.IO) {
                mTracks.value?.indexOfFirst { it.trackId == track.id } ?: -1
            }
            if (currentIdx != -1) {
                mSelectedTrackIdx.value = currentIdx
            }
        }
    }
}