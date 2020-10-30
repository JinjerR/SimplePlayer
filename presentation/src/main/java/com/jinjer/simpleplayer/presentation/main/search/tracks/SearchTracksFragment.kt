package com.jinjer.simpleplayer.presentation.main.search.tracks

import com.jinjer.simpleplayer.presentation.controller.service.QueueData
import com.jinjer.simpleplayer.presentation.main.search.SearchType
import com.jinjer.simpleplayer.presentation.main.search.base.SearchFragmentBase
import com.jinjer.simpleplayer.presentation.main.tracks.recycler_view.TrackViewHolder
import com.jinjer.simpleplayer.presentation.main.tracks.TrackPresenter
import com.jinjer.simpleplayer.presentation.utils.extensions.fragmentViewModel

class SearchTracksFragment: SearchFragmentBase<TrackPresenter, TrackViewHolder>() {

    override val adapter = SearchTracksAdapter(::onItemClick)

    override val searchViewModel: SearchTracksViewModel by fragmentViewModel()

    override val searchType: SearchType = SearchType.TRACKS

    override fun subscribeToViewModel() {
        super.subscribeToViewModel()

        mainViewModel.isPlaying.observe(viewLifecycleOwner) { isPlaying ->
            adapter.setIsPlaying(isPlaying)
        }
    }

    private fun onItemClick(track: TrackPresenter) {
        mainViewModel.play(track.trackId, QueueData.buildAllTracksData())
    }
}