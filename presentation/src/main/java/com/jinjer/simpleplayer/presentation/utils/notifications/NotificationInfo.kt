package com.jinjer.simpleplayer.presentation.utils.notifications

import android.graphics.Bitmap
import android.support.v4.media.session.MediaSessionCompat

data class NotificationInfo(
    val trackName: String,
    val bandName: String,
    val isPlaying: Boolean,
    val albumImage: Bitmap?,
    val token: MediaSessionCompat.Token)