package com.jinjer.simpleplayer.presentation.main.search.tracks

import com.jinjer.simpleplayer.presentation.controller.service.QueueData
import com.jinjer.simpleplayer.presentation.main.search.SearchType
import com.jinjer.simpleplayer.presentation.main.search.base.SearchFragmentBase
import com.jinjer.simpleplayer.presentation.main.tracks.recycler_view.TrackViewHolder
import com.jinjer.simpleplayer.presentation.models.track.Track
import com.jinjer.simpleplayer.presentation.utils.extensions.fragmentViewModel

class SearchTracksFragment: SearchFragmentBase<Track, TrackViewHolder>() {

    override val searchAdapter = SearchTracksAdapter(::onItemClick)

    override val searchViewModel: SearchTracksViewModel by fragmentViewModel()

    override val searchType: SearchType = SearchType.TRACKS

    override fun subscribeToMainViewModel() {
        super.subscribeToMainViewModel()

        mainViewModel.isPlaying.observe(viewLifecycleOwner) { isPlaying ->
            searchAdapter.setPlaying(isPlaying)
        }
        mainViewModel.currentTrack.observe(viewLifecycleOwner) { track ->
            searchAdapter.onTrackChanged(track.id)
        }
    }

    private fun onItemClick(track: Track) {
        mainViewModel.play(track.trackId, QueueData.buildSearchData())
    }
}