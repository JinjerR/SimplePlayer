package com.jinjer.simpleplayer.presentation.main.tracks.recycler_view

import android.view.View
import com.jinjer.simpleplayer.presentation.R
import com.jinjer.simpleplayer.presentation.main.tracks.TrackPresenter
import com.jinjer.simpleplayer.presentation.utils.recyclerview.BaseAdapter

abstract class TracksAdapterBase(
    itemClick: (TrackPresenter) -> Unit
): BaseAdapter<TrackPresenter, TrackViewHolder>(TrackDiff(), itemClick) {

    private var currentTrackId = -1
    private var currentPosition = -1
    private var isPlaying = false

    override var layoutId: Int = R.layout.item_track

    override fun initHolder(view: View) = TrackViewHolder(view)

    override fun onItemClick(data: TrackPresenter, position: Int) {
        selectTrackAt(position)
        super.onItemClick(data, position)
    }

    override fun submitList(list: List<TrackPresenter>?) {
        val idx = list?.indexOfFirst { it.trackId == currentTrackId } ?: -1

        currentPosition = if (idx == -1) {
            updateTrackAt(currentPosition, selected = false, playing = false, notify = false)
            -1
        } else {
            list?.get(idx)?.let {
                it.isSelected = true
                it.isPlaying = isPlaying
            }
            idx
        }

        super.submitList(list)
    }

    fun setPlaying(playing: Boolean) {
        updateTrackAt(currentPosition, playing = playing)
        isPlaying = playing
    }

    fun onTrackChanged(trackId: Int) {
        val idx = currentList.indexOfFirst { it.trackId == trackId }
        selectTrackAt(idx)

        currentTrackId = trackId
    }

    private fun selectTrackAt(newPosition: Int) {
        updateTrackAt(currentPosition, selected = false, playing = false)
        updateTrackAt(newPosition, selected = true, playing = isPlaying)
        currentPosition = newPosition
    }

    private fun updateTrackAt(position: Int, selected: Boolean? = null, playing: Boolean? = null, notify: Boolean = true) {
        if (position == -1 || position >= itemCount) {
            return
        }

        getItem(position).let { track ->
            selected?.let { track.isSelected = it }
            playing?.let { track.isPlaying = it }
        }

        if (notify) {
            notifyItemChanged(position)
        }
    }
}