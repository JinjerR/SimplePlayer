package com.jinjer.simpleplayer.presentation.main.tracks

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TrackPresenter(
    val trackId: Int,
    val trackName: String,
    val bandName: String,
    var isSelected: Boolean = false,
    var isPlaying: Boolean = false
): Parcelable