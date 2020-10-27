package com.jinjer.simpleplayer.presentation.main.tracks.recycler_view

import android.view.View
import com.jinjer.simpleplayer.presentation.R
import com.jinjer.simpleplayer.presentation.main.tracks.TrackPresenter
import com.jinjer.simpleplayer.presentation.utils.recyclerview.BaseAdapter

abstract class TracksAdapterBase(
    itemClick: (TrackPresenter) -> Unit
): BaseAdapter<TrackPresenter, TrackViewHolder>(TrackDiff(), itemClick) {

    private var previousPosition = -1
    private var currentPosition = -1
    private var isPlaying = false

    override var layoutId: Int = R.layout.item_track

    override fun initHolder(view: View) = TrackViewHolder(view)

    override fun onItemClick(data: TrackPresenter, position: Int) {
        setSelected(position)
        super.onItemClick(data, position)
    }

    fun setIsPlaying(playing: Boolean) {
        isPlaying = playing

        if (currentPosition != -1) {
            getItem(currentPosition).isPlaying = isPlaying
            notifyItemChanged(currentPosition)
        }
    }

    protected fun setSelected(newPosition: Int) {
        previousPosition = currentPosition
        currentPosition = newPosition

        val currentTrack = getItem(currentPosition)
        currentTrack.isSelected = true
        currentTrack.isPlaying = isPlaying

        notifyItemChanged(currentPosition)

        if (previousPosition != -1 && previousPosition != currentPosition) {
            val previousTrack = getItem(previousPosition)
            previousTrack.isSelected = false
            previousTrack.isPlaying = false
            notifyItemChanged(previousPosition)
        }
    }
}