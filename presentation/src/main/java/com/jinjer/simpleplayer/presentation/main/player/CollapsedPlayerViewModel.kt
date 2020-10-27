package com.jinjer.simpleplayer.presentation.main.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.jinjer.simpleplayer.presentation.controller.main.IClientController
import com.jinjer.simpleplayer.presentation.controller.main.IPlayerController

class CollapsedPlayerViewModel(
    playerController: IPlayerController
): PlayerFragmentViewModelBase(playerController) {
    val bandName: LiveData<String> = Transformations.map(playerController.currentTrack) { track ->
        track.artist
    }
}
