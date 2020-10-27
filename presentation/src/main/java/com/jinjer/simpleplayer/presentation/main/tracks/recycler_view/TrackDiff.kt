package com.jinjer.simpleplayer.presentation.main.tracks.recycler_view

import androidx.recyclerview.widget.DiffUtil
import com.jinjer.simpleplayer.presentation.main.tracks.TrackPresenter

class TrackDiff: DiffUtil.ItemCallback<TrackPresenter>() {
    override fun areContentsTheSame(
        oldItem: TrackPresenter,
        newItem: TrackPresenter
    ): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(
        oldItem: TrackPresenter,
        newItem: TrackPresenter
    ): Boolean {
        return oldItem.trackId == newItem.trackId
    }
}