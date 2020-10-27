package com.jinjer.simpleplayer.presentation.main.tracks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.jinjer.simpleplayer.presentation.R
import com.jinjer.simpleplayer.presentation.base.BaseFragment
import com.jinjer.simpleplayer.presentation.databinding.FragmentTracksBinding
import com.jinjer.simpleplayer.presentation.main.tracks.recycler_view.TracksAdapter
import com.jinjer.simpleplayer.presentation.utils.ShowLog
import com.jinjer.simpleplayer.presentation.utils.extensions.fragmentViewModel

class TracksFragment : BaseFragment() {
    private val tracksViewModel: TracksViewModel by fragmentViewModel()
    private lateinit var binding: FragmentTracksBinding
    private val tracksAdapter = TracksAdapter(::onTrackClicked)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tracks, container, false)
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        tracksViewModel.tracks.observe(viewLifecycleOwner) { tracks ->
            tracksAdapter.submitList(tracks)
        }
//        tracksViewModel.isPlaying.observe(viewLifecycleOwner) { isPlaying ->
//            ShowLog.i("TracksFragment isPlaying observer, isPlaying = $isPlaying", ShowLog.tagTest)
//            tracksAdapter.setIsPlaying(isPlaying)
//        }
        tracksViewModel.currentIdx.observe(viewLifecycleOwner) { trackIdx ->
            tracksAdapter.setSelectedByIdx(trackIdx)
        }
    }

    private fun initRecyclerView() {
        with(binding.tracks) {
            adapter = tracksAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun onTrackClicked(track: TrackPresenter) {
        tracksViewModel.playTrack(track.trackId)
    }
}