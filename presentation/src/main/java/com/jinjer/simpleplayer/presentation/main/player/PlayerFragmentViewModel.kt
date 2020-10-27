package com.jinjer.simpleplayer.presentation.main.player

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.jinjer.simpleplayer.presentation.controller.main.IPlayerController
import com.jinjer.simpleplayer.presentation.utils.Utils

class PlayerFragmentViewModel(private val playerController: IPlayerController): PlayerFragmentViewModelBase(playerController) {

    val duration: LiveData<Long> = Transformations.map(playerController.currentTrack) { track ->
        track.duration ?: 0L
    }

    val albumArtUri: LiveData<Uri> = Transformations.map(playerController.currentTrack) { track ->
        val albumId = track.albumId
        if (albumId == -1) {
            null
        } else {
            Utils.getAlbumArtUri(track.albumId.toLong())
        }
    }

    val currentSecondsProgress: LiveData<Int> = Transformations.map(playerController.currentPosition) { position ->
        (position / 1000).toInt()
    }

    fun onSkipToPreviousClicked() {
        playerController.skipToPrevious()
    }

    fun onSkipToNextClicked() {
        playerController.skipToNext()
    }

    fun rewind(newPosition: Int) {
        playerController.seekTo(newPosition)
    }
}