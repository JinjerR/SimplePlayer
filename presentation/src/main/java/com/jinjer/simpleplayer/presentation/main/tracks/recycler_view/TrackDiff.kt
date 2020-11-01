package com.jinjer.simpleplayer.presentation.main.tracks.recycler_view

import androidx.recyclerview.widget.DiffUtil
import com.jinjer.simpleplayer.presentation.models.track.Track

class TrackDiff: DiffUtil.ItemCallback<Track>() {
    override fun areContentsTheSame(
        oldItem: Track,
        newItem: Track
    ): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(
        oldItem: Track,
        newItem: Track
    ): Boolean {
        return oldItem.trackId == newItem.trackId
    }
}