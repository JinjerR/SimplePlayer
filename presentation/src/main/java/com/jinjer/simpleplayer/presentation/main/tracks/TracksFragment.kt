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
import com.jinjer.simpleplayer.presentation.controller.service.QueueType
import com.jinjer.simpleplayer.presentation.databinding.FragmentTracksBinding
import com.jinjer.simpleplayer.presentation.main.tracks.recycler_view.TracksAdapter
import com.jinjer.simpleplayer.presentation.models.track.Track
import com.jinjer.simpleplayer.presentation.utils.ShowLog
import com.jinjer.simpleplayer.presentation.utils.ShowLog.tagTest
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

    fun updateData(tracks: List<Track>, queueData: QueueData? = null) {
        queueData?.let {
            this.queueData = queueData
        }

        tracksAdapter.submitList(tracks)
    }

    private fun subscribeToFragmentViewModel() {
        tracksViewModel.tracks.observe(viewLifecycleOwner) { tracks ->
            tracksAdapter.submitList(tracks)
        }
    }

    private fun subscribeToMainViewModel() {
        mainViewModel.isPlaying.observe(viewLifecycleOwner) { isPlaying ->
            tracksAdapter.setPlaying(isPlaying)
        }
        mainViewModel.isTracksLoaded.observe(viewLifecycleOwner) { isTracksLoaded ->
            if (isTracksLoaded) {
                if (queueData?.type == QueueType.ALL_TRACKS) {
                    tracksViewModel.showAllTracks()
                } else {
                    arguments?.getParcelableArrayList<Track>(keyTracks)?.let {
                        tracksAdapter.submitList(it)
                    }
                }
            }
        }
        mainViewModel.currentTrack.observe(viewLifecycleOwner) { track ->
            tracksAdapter.onTrackChanged(track.id)
        }
    }

    private fun initRecyclerView() {
        with(binding.tracks) {
            adapter = tracksAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun onTrackClicked(track: Track) {
        mainViewModel.play(track.trackId, queueData ?: QueueData.buildAllTracksData())
    }

    companion object {
        const val keyTracks = "key_tracks"

        fun newInstance(tracks: List<Track>? = null, queueData: QueueData? = null): TracksFragment = TracksFragment().apply {
            arguments = Bundle().apply {
                tracks?.let { putParcelableArrayList(keyTracks, ArrayList(it)) }
                queueData?.let { putParcelable(PlayerNavigator.keyQueueData, queueData) }
            }
        }
    }
}