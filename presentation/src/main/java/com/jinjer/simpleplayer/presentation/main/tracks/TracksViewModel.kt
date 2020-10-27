package com.jinjer.simpleplayer.presentation.main.tracks

import androidx.lifecycle.*
import com.jinjer.simpleplayer.domain.usecases.GetTracksUseCase
import com.jinjer.simpleplayer.presentation.controller.main.IPlayerController
import com.jinjer.simpleplayer.presentation.models.mappers.TrackPresenterMapper
import com.jinjer.simpleplayer.presentation.utils.ShowLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TracksViewModel(
    playerController: IPlayerController,
    private val getTracks: GetTracksUseCase,
    private val mapper: TrackPresenterMapper): ViewModel() {

    private val mTracks = MutableLiveData<List<TrackPresenter>>()
    val tracks: LiveData<List<TrackPresenter>> = mTracks

//    val isPlaying: LiveData<Boolean> = playerController.isPlaying

    private val tracksLoadingObserver: Observer<Boolean> = Observer { isLoaded ->
        if (isLoaded) {
            viewModelScope.launch {
                mTracks.value = withContext(Dispatchers.IO) {
                    mapper.fromList(getTracks())
                }
            }
        }
    }

    private val controller = playerController.also { controller ->
        controller.isTracksLoaded.observeForever(tracksLoadingObserver)
    }

    // since the track changes not only from the current list, it is necessary to listen to its changes
    // from other places too (for example, from the track playback window, the "next track" button),
    // the track is converted to its position in the current list in order to transfer this index
    // to the adapter and make it selected.
    val currentIdx = MediatorLiveData<Int>().apply {
        addSource(controller.currentTrack) { track ->
            viewModelScope.launch {
                val currentIdx = withContext(Dispatchers.IO) {
                    mTracks.value?.indexOfFirst { it.trackId == track.id } ?: -1
                }
                if (currentIdx != -1) {
                    value = currentIdx
                }
            }
        }
    }

    fun playTrack(trackId: Int) {
        controller.play(trackId)
    }

    override fun onCleared() {
        controller.isTracksLoaded.removeObserver(tracksLoadingObserver)
    }
}