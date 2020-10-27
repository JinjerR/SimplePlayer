package com.jinjer.simpleplayer.presentation.controller.client

interface IClientManager {
    fun connect()

    fun disconnect()

    fun sendEmptyMessage(action: Int)
}