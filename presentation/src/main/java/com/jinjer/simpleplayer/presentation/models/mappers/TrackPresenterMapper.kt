package com.jinjer.simpleplayer.presentation.models.mappers

import com.jinjer.simpleplayer.domain.utils.Mapper
import com.jinjer.simpleplayer.domain.models.TrackDomain
import com.jinjer.simpleplayer.presentation.main.tracks.TrackPresenter

class TrackPresenterMapper: Mapper<TrackDomain, TrackPresenter>() {
    override fun from(element: TrackDomain): TrackPresenter {
        return TrackPresenter(
            element.id,
            element.artist ?: "",
            element.title ?: ""
        )
    }
}