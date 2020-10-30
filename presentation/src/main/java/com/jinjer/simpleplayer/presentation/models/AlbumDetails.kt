package com.jinjer.simpleplayer.presentation.models

import android.net.Uri
import android.os.Parcelable
import com.jinjer.simpleplayer.presentation.main.tracks.TrackPresenter
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AlbumDetails(
    val id: Int,
    val title: String,
    val trackList: List<TrackPresenter>,
    val tracksNumber: Int,
    val albumArtUri: Uri
): Parcelable
