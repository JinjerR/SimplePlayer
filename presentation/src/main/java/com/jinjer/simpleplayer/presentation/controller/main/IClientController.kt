package com.jinjer.simpleplayer.presentation.controller.main

interface IClientController: IPlayerController {
    fun connect()

    fun disconnect()

    fun sendEmptyMessage(action: Int)
}