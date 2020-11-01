package com.jinjer.simpleplayer.presentation.models.album

import android.net.Uri
import android.os.Parcelable
import com.jinjer.simpleplayer.presentation.models.track.Track
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AlbumDetails(
    val id: Int,
    val title: String,
    val trackList: List<Track>,
    val tracksNumber: Int,
    val albumArtUri: Uri
): Parcelable
