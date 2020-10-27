package com.jinjer.simpleplayer.presentation.main.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.jinjer.simpleplayer.presentation.R
import com.jinjer.simpleplayer.presentation.controller.main.IPlayerController

open class PlayerFragmentViewModelBase(private val playerController: IPlayerController): ViewModel() {

    val trackTitle: LiveData<String> = Transformations.map(playerController.currentTrack) { track ->
        track.title
    }

//    val playIconResId: LiveData<Int> = Transformations.map(playerController.isPlaying) { isPlaying ->
//        if (isPlaying) {
//            R.drawable.icon_pause
//        } else {
//            R.drawable.icon_play
//        }
//    }

    fun onPlayPauseClicked() {
        playerController.play(playerController.currentTrack.value?.id ?: -1)
    }
}