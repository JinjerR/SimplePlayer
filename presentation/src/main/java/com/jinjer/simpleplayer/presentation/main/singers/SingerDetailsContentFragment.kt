package com.jinjer.simpleplayer.presentation.main.singers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.jinjer.simpleplayer.presentation.R
import com.jinjer.simpleplayer.presentation.base.BaseFragment
import com.jinjer.simpleplayer.presentation.controller.service.QueueData
import com.jinjer.simpleplayer.presentation.databinding.FragmentSingerDetailsBinding
import com.jinjer.simpleplayer.presentation.main.search.ItemAlbumType
import com.jinjer.simpleplayer.presentation.main.search.albums.SearchAlbumsAdapter
import com.jinjer.simpleplayer.presentation.main.tracks.TracksFragment
import com.jinjer.simpleplayer.presentation.models.album.Album
import com.jinjer.simpleplayer.presentation.utils.custom_components.RecyclerItemDecoration
import com.jinjer.simpleplayer.presentation.utils.extensions.fragmentViewModel

class SingerDetailsContentFragment: BaseFragment() {
    private lateinit var binding: FragmentSingerDetailsBinding
    private val singerViewModel: SingerDetailsContentViewModel by fragmentViewModel()
    private val tagTracks = "tag_tracks"
    private val albumsAdapter = SearchAlbumsAdapter(ItemAlbumType.HORIZONTAL, ::onAlbumClicked)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_singer_details, container, false)
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addTracksFragment()
        initAlbumsRecyclerView()

        val id = arguments?.getInt(SingerDetailsFragment.keySingerId, -1) ?: -1
        singerViewModel.getSingerDetails(id)

        singerViewModel.singerDetails.observe(viewLifecycleOwner) { singerDetails ->
            with(singerDetails) {
                val trackIds = trackList.map { it.trackId }
                val queueData = QueueData.buildSingerData(singerId, trackIds)

                getTracksFragment()
                    ?.updateData(singerDetails.trackList, queueData)

                albumsAdapter.submitList(singerDetails.albumList)

                binding.txtTitle.text = singerDetails.singerName
            }
        }
    }

    private fun initAlbumsRecyclerView() {
        with(binding.recyclerAlbums) {
            adapter = albumsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            val horizontalSpace = resources.getDimension(R.dimen.space_half)
            addItemDecoration(RecyclerItemDecoration(spaceHorizontal = horizontalSpace.toInt()))
        }
    }

    private fun onAlbumClicked(album: Album) {
        (parentFragment as? IOnAlbumClickedListener)?.onAlbumClicked(album.albumId)
    }

    private fun addTracksFragment() {
        getTracksFragment() ?: run {
            val tracksFragment = TracksFragment.newInstance()

            childFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_tracks, tracksFragment, tagTracks)
                .commit()
        }
    }

    private fun getTracksFragment(): TracksFragment? =
        childFragmentManager.findFragmentByTag(tagTracks) as? TracksFragment

    companion object {
        fun newInstance(singerId: Int) = SingerDetailsContentFragment().apply {
            arguments = Bundle().apply {
                putInt(SingerDetailsFragment.keySingerId, singerId)
            }
        }
    }
}