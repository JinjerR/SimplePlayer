package com.jinjer.simpleplayer.presentation.main.search.tracks

import com.jinjer.simpleplayer.presentation.main.tracks.recycler_view.TracksAdapterBase
import com.jinjer.simpleplayer.presentation.main.tracks.TrackPresenter

class SearchTracksAdapter(
    itemClick: (TrackPresenter) -> Unit
): TracksAdapterBase(itemClick)