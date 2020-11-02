package com.jinjer.simpleplayer.presentation.main.player

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.jinjer.simpleplayer.presentation.R
import com.jinjer.simpleplayer.presentation.bottom_sheet.IBottomSheetParent
import com.jinjer.simpleplayer.presentation.databinding.FragmentPlayerBinding
import com.jinjer.simpleplayer.presentation.models.track.TrackData
import com.jinjer.simpleplayer.presentation.utils.TimeUtils
import com.jinjer.simpleplayer.presentation.utils.Utils
import com.jinjer.simpleplayer.presentation.utils.extensions.roundUpTwoDecimalPlaces
import com.jinjer.simpleplayer.presentation.utils.extensions.toSeconds

class NowPlayingFragment: NowPlayingFragmentBase(), SeekBar.OnSeekBarChangeListener {

    private val tagPlayerCollapsed = "tag_player_collapsed"

    private lateinit var binding: FragmentPlayerBinding

    private var trackingTouch: Boolean = false
    private var collapsedPlayerAlpha = 0f
    private var bottomSheetParent: IBottomSheetParent? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_player, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetParent = parentFragment as? IBottomSheetParent
        bottomSheetParent?.addBottomSheetCallback(bottomSheetCallback)

        addPlayerCollapsed()

        binding.progressBar.setOnSeekBarChangeListener(this)
        binding.fragmentPlayerCollapsed.setOnClickListener(::onCollapsedPlayerClicked)
        binding.pausePlay.setOnClickListener(::onPlayPauseClicked)

        binding.rewindForward.setOnClickListener {
            mainViewModel.skipToNext()
        }
        binding.rewindBack.setOnClickListener {
            mainViewModel.skipToPrevious()
        }

        subscribeToMainViewModel()
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        binding.durationStart.text = TimeUtils.getFormattedSongDurationFromSeconds(progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        trackingTouch = true
    }
    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        trackingTouch = false
        mainViewModel.seekTo(seekBar?.progress ?: 0)
    }

    override fun onTrackChanged(track: TrackData) {
        if (track.albumId != -1) {
            val albumArt = Utils.getAlbumArtUri(track.albumId.toLong())
            loadAlbumArt(albumArt)
        }

        val duration = track.duration
        binding.durationEnd.text = TimeUtils.getFormattedDurationFromMillis(duration)
        binding.name.text = track.title
        binding.progressBar.max = track.duration.toSeconds()
    }

    override fun onPlayIconChanged(resId: Int) {
        binding.pausePlay.setImageResource(resId)
    }

    override fun subscribeToMainViewModel() {
        super.subscribeToMainViewModel()

        mainViewModel.currentPosition.observe(viewLifecycleOwner) { position ->
            if (trackingTouch) {
                return@observe
            }

            binding.progressBar.progress = position.toSeconds()
        }
    }

    private val bottomSheetCallback = object: BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            when(newState) {
                BottomSheetBehavior.STATE_EXPANDED -> {
                    binding.fragmentPlayerCollapsed.visibility = View.INVISIBLE
                }
                else -> {
                    binding.fragmentPlayerCollapsed.visibility = View.VISIBLE
                }
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            var alpha = (1f - (slideOffset / 0.5f)).roundUpTwoDecimalPlaces()
            if (alpha < 0) {
                alpha = 0f
            }

            if (collapsedPlayerAlpha == alpha) {
                return
            }

            collapsedPlayerAlpha = alpha

            binding.fragmentPlayerCollapsed.alpha = collapsedPlayerAlpha
        }
    }

    private fun loadAlbumArt(uri: Uri) {
        with(binding.albumImage) {
            Glide.with(this)
                .load(uri)
                .error(R.drawable.icon_empty_album_art)
                .placeholder(R.drawable.icon_empty_album_art)
                .into(this)
        }
    }

    private fun addPlayerCollapsed() {
        childFragmentManager.findFragmentByTag(tagPlayerCollapsed) ?: run {
            val fragment = CollapsedPlayerFragment()

            childFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_player_collapsed, fragment, tagPlayerCollapsed)
                .commit()
        }
    }

    private fun onCollapsedPlayerClicked(@Suppress("UNUSED_PARAMETER") view: View) {
        bottomSheetParent?.expandBottomSheet()
    }
}
