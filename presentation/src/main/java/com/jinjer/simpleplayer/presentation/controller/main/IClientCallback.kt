package com.jinjer.simpleplayer.presentation.controller.main

import android.support.v4.media.session.MediaSessionCompat

interface IClientCallback {
    fun onConnectedToService(sessionToken: MediaSessionCompat.Token)

    fun onTracksLoaded()
}