package com.jinjer.simpleplayer.presentation.main.tracks.recycler_view

import com.jinjer.simpleplayer.presentation.main.tracks.TrackPresenter

class TracksAdapter(itemCLick: (TrackPresenter) -> Unit)
    : TracksAdapterBase(itemCLick) {

    fun setSelectedByIdx(idx: Int) {
        setSelected(idx)
    }
}