package com.jinjer.simpleplayer.presentation.main.search.tracks

import com.jinjer.simpleplayer.presentation.main.tracks.recycler_view.TracksAdapterBase
import com.jinjer.simpleplayer.presentation.models.track.Track

class SearchTracksAdapter(
    itemClick: (Track) -> Unit
): TracksAdapterBase(itemClick)