package com.jinjer.simpleplayer.presentation.base

import androidx.lifecycle.ViewModel
import com.jinjer.simpleplayer.presentation.controller.main.IClientController
import com.jinjer.simpleplayer.presentation.controller.service.MusicService

class MainActivityViewModel(private val clientController: IClientController): ViewModel() {

    fun onPermissionGranted() {
        clientController.connect()
    }

    fun onAppResumes() {
       clientController.sendEmptyMessage(MusicService.appResumes)
    }

    override fun onCleared() {
        super.onCleared()
        clientController.disconnect()
    }
}