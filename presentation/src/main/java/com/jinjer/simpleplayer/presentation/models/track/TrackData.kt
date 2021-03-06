package com.jinjer.simpleplayer.presentation.models.track

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TrackData (
    val id: Int,
    val title: String,
    val artist: String,
    val artistId: Int,
    val album: String,
    val albumId: Int,
    val duration: Long
): Parcelable {
    companion object {
        fun emptyTrack(): TrackData {
            return TrackData(-1, "", "", -1, "", -1, -1L)
        }
    }
}