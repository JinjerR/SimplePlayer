package com.jinjer.simpleplayer.presentation.main.search.albums

import android.net.Uri

data class SearchAlbumPresenter(
    val albumId: Int,
    val albumName: String,
    val albumUri: Uri)