package com.jinjer.simpleplayer.presentation.main.player

import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import com.jinjer.simpleplayer.presentation.R
import com.jinjer.simpleplayer.presentation.base.BaseFragment
import com.jinjer.simpleplayer.presentation.models.Track

abstract class NowPlayingFragmentBase: BaseFragment() {

    protected fun onPlayPauseClicked(view: View) {
        mainViewModel.resume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToMainViewModel()
    }

    protected open fun subscribeToMainViewModel() {
        mainViewModel.currentTrack.observe(viewLifecycleOwner) { track ->
            onTrackChanged(track)
        }
        mainViewModel.isPlaying.observe(viewLifecycleOwner) { playing ->
            val resId =
                if (playing) R.drawable.icon_pause
                else R.drawable.icon_play

            onPlayIconChanged(resId)
        }
    }

    abstract fun onTrackChanged(track: Track)

    abstract fun onPlayIconChanged(@DrawableRes resId: Int)
}