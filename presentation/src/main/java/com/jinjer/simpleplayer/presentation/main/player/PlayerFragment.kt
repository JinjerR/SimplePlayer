package com.jinjer.simpleplayer.presentation.main.player

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.jinjer.simpleplayer.presentation.R
import com.jinjer.simpleplayer.presentation.base.BaseFragment
import com.jinjer.simpleplayer.presentation.bottom_sheet.IBottomSheetParent
import com.jinjer.simpleplayer.presentation.databinding.FragmentPlayerBinding
import com.jinjer.simpleplayer.presentation.utils.ShowLog
import com.jinjer.simpleplayer.presentation.utils.TimeUtils
import com.jinjer.simpleplayer.presentation.utils.extensions.fragmentViewModel
import com.jinjer.simpleplayer.presentation.utils.extensions.roundUpTwoDecimalPlaces

class PlayerFragment : BaseFragment(), SeekBar.OnSeekBarChangeListener {

    private val playerViewModel: PlayerFragmentViewModel by fragmentViewModel()
    private lateinit var binding: FragmentPlayerBinding

    private var trackingTouch: Boolean = false
    private var collapsedPlayerAlpha = 0f
    private var bottomSheetParent: IBottomSheetParent? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        bottomSheetParent = context as? IBottomSheetParent
        bottomSheetParent?.addBottomSheetCallback(bottomSheetCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_player, container, false)
        binding.viewModel = playerViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addPlayerCollapsed()

        binding.progress.setOnSeekBarChangeListener(this)
        binding.fragmentPlayerCollapsed.setOnClickListener(::onCollapsedPlayerClicked)

        subscribeToViewModel()
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        binding.durationStart.text = TimeUtils.getFormattedSongDurationFromSeconds(progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        trackingTouch = true
    }
    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        trackingTouch = false
        playerViewModel.rewind(seekBar?.progress ?: 0)
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

    private fun subscribeToViewModel() {
        playerViewModel.currentSecondsProgress.observe(viewLifecycleOwner) { seconds ->
            if (trackingTouch) {
                return@observe
            }

            binding.progress.progress = seconds
        }
    }

    private fun addPlayerCollapsed() {
        val fragment = CollapsedPlayerFragment()

        childFragmentManager
            .beginTransaction()
            .add(R.id.fragment_player_collapsed, fragment, null)
            .commit()
    }

    private fun onCollapsedPlayerClicked(@Suppress("UNUSED_PARAMETER") view: View) {
        bottomSheetParent?.expandBottomSheet()
    }
}
