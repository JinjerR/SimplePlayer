package com.jinjer.simpleplayer.presentation.controller.client

import android.support.v4.media.session.MediaSessionCompat

interface IClientCallback {
    fun onTokenReceived(token: MediaSessionCompat.Token)
}