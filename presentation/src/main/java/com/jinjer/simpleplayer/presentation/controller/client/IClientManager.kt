package com.jinjer.simpleplayer.presentation.controller.client

interface IClientManager {
    fun startService()

    fun stopService()

    fun bind()

    fun unbind()

    fun sendMessageToService(action: Int)
}