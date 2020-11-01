package com.jinjer.simpleplayer.presentation.main.albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.jinjer.simpleplayer.presentation.R
import com.jinjer.simpleplayer.presentation.base.BaseFragment
import com.jinjer.simpleplayer.presentation.controller.service.QueueData
import com.jinjer.simpleplayer.presentation.databinding.FragmentAlbumDetailsBinding
import com.jinjer.simpleplayer.presentation.main.albums.AlbumDetailsFragment.Companion.keyAlbumId
import com.jinjer.simpleplayer.presentation.models.track.Track
import com.jinjer.simpleplayer.presentation.main.tracks.TracksFragment
import com.jinjer.simpleplayer.presentation.utils.Utils
import com.jinjer.simpleplayer.presentation.utils.extensions.fragmentViewModel

class AlbumDetailsContentFragment: BaseFragment() {
    private val albumDetailsViewModel: AlbumDetailsContentViewModel by fragmentViewModel()
    private lateinit var binding: FragmentAlbumDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_album_details, container, false)
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        albumDetailsViewModel.albumDetails.observe(viewLifecycleOwner) { albumDetails ->
            val trackIds = albumDetails.trackList.map { it.trackId }
            val queueData = QueueData.buildAlbumData(albumDetails.id, trackIds)

            addTracksFragment(albumDetails.trackList, queueData)
            loadAlbumImage(albumDetails.id)
            binding.txtAlbumName.text = albumDetails.title
        }

        val albumId = arguments?.getInt(keyAlbumId, -1) ?: -1
        albumDetailsViewModel.getAlbumDetails(albumId)
    }

    private fun addTracksFragment(tracks: List<Track>, queueData: QueueData) {
        val tracksFragment = TracksFragment.newInstance(tracks, queueData)

        childFragmentManager
            .beginTransaction()
            .add(R.id.fragment_tracks, tracksFragment)
            .commit()
    }

    private fun loadAlbumImage(albumId: Int) {
        val imgUri = Utils.getAlbumArtUri(albumId.toLong())

        with(binding.imgAlbum) {
            Glide.with(this)
                .load(imgUri)
                .error(R.drawable.icon_empty_album_art_round)
                .placeholder(R.drawable.icon_empty_album_art_round)
                .into(this)
        }
    }

    companion object {
        fun newInstance(albumId: Int): AlbumDetailsContentFragment = AlbumDetailsContentFragment().apply {
            arguments = Bundle().apply {
                putInt(keyAlbumId, albumId)
            }
        }
    }
}