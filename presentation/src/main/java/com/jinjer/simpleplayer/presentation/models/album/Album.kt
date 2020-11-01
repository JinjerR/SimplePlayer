package com.jinjer.simpleplayer.presentation.models.album

import android.net.Uri

data class Album(
    val albumId: Int,
    val albumName: String,
    val albumUri: Uri)