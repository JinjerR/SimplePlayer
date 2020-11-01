package com.jinjer.simpleplayer.presentation.main.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.jinjer.simpleplayer.presentation.R
import com.jinjer.simpleplayer.presentation.databinding.FragmentCollapsedPlayerBinding
import com.jinjer.simpleplayer.presentation.models.track.TrackData

class CollapsedPlayerFragment: NowPlayingFragmentBase() {
    private lateinit var binding: FragmentCollapsedPlayerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_collapsed_player, container, false)
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgPlay.setOnClickListener(::onPlayPauseClicked)
    }

    override fun onTrackChanged(track: TrackData) {
        binding.trackName.text = track.title
        binding.bandName.text = track.artist
    }

    override fun onPlayIconChanged(resId: Int) {
        binding.imgPlay.setImageResource(resId)
    }
}