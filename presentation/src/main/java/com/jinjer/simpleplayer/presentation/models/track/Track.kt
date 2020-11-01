package com.jinjer.simpleplayer.presentation.models.track

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Track(
    val trackId: Int,
    val trackName: String,
    val bandName: String,
    var isSelected: Boolean = false,
    var isPlaying: Boolean = false
): Parcelable