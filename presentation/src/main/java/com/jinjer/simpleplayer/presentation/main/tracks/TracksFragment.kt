package com.jinjer.simpleplayer.presentation.main.tracks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.jinjer.simpleplayer.presentation.R
import com.jinjer.simpleplayer.presentation.base.BaseFragment
import com.jinjer.simpleplayer.presentation.controller.service.PlayerNavigator
import com.jinjer.simpleplayer.presentation.controller.service.QueueData
import com.jinjer.simpleplayer.presentation.databinding.FragmentTracksBinding
import com.jinjer.simpleplayer.presentation.main.tracks.recycler_view.TracksAdapter
import com.jinjer.simpleplayer.presentation.utils.extensions.fragmentViewModel

class TracksFragment : BaseFragment() {

    private val tracksViewModel: TracksViewModel by fragmentViewModel()
    private lateinit var binding: FragmentTracksBinding
    private val tracksAdapter = TracksAdapter(::onTrackClicked)

    private var queueData: QueueData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        queueData = arguments?.getParcelable(PlayerNavigator.keyQueueData)
    }

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

        subscribeToFragmentViewModel()
        subscribeToMainViewModel()
    }

    private fun subscribeToFragmentViewModel() {
        tracksViewModel.selectedTrackIdx.observe(viewLifecycleOwner) { trackIdx ->
            tracksAdapter.setSelectedByIdx(trackIdx)
        }
        tracksViewModel.tracks.observe(viewLifecycleOwner) { tracks ->
            tracksAdapter.submitList(tracks)
        }
    }

    private fun subscribeToMainViewModel() {
        mainViewModel.isPlaying.observe(viewLifecycleOwner) { isPlaying ->
            tracksAdapter.setIsPlaying(isPlaying)
        }
        mainViewModel.isTracksLoaded.observe(viewLifecycleOwner) { isTracksLoaded ->
            if (isTracksLoaded) {
                val tracks: List<TrackPresenter>? = arguments?.getParcelableArrayList(keyTracks)
                tracksViewModel.onTracksLoaded(tracks)
            }
        }
        mainViewModel.currentTrack.observe(viewLifecycleOwner) { track ->
            tracksViewModel.onTrackChanged(track)
        }
    }

    private fun initRecyclerView() {
        with(binding.tracks) {
            adapter = tracksAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun onTrackClicked(track: TrackPresenter) {
        mainViewModel.play(track.trackId, queueData ?: QueueData.buildAllTracksData())
    }

    companion object {
        const val keyTracks = "key_tracks"

        fun newInstance(tracks: List<TrackPresenter>? = null, queueData: QueueData): TracksFragment = TracksFragment().apply {
            arguments = Bundle().apply {
                tracks?.let { putParcelableArrayList(keyTracks, ArrayList(it)) }
                putParcelable(PlayerNavigator.keyQueueData, queueData)
            }
        }
    }
}