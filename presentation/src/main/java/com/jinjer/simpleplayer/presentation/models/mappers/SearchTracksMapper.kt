package com.jinjer.simpleplayer.presentation.models.mappers

import com.jinjer.simpleplayer.domain.models.TrackDomain
import com.jinjer.simpleplayer.domain.utils.Mapper
import com.jinjer.simpleplayer.presentation.main.tracks.TrackPresenter

class SearchTracksMapper: Mapper<TrackDomain, TrackPresenter>() {
    override fun from(element: TrackDomain): TrackPresenter =
        TrackPresenter(
            element.id,
            element.title ?: "",
            element.artist ?: ""
        )
}