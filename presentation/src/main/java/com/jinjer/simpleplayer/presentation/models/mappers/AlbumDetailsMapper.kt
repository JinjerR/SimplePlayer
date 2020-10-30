package com.jinjer.simpleplayer.presentation.models.mappers

import com.jinjer.simpleplayer.domain.models.AlbumDetailsDomain
import com.jinjer.simpleplayer.domain.utils.Mapper
import com.jinjer.simpleplayer.presentation.models.AlbumDetails
import com.jinjer.simpleplayer.presentation.utils.Utils

class AlbumDetailsMapper: Mapper<AlbumDetailsDomain, AlbumDetails>() {
    private val trackPresenterMapper = TrackPresenterMapper()

    override fun from(element: AlbumDetailsDomain): AlbumDetails {
        return with(element) {
            AlbumDetails(
                id = id,
                title = title,
                trackList = trackPresenterMapper.fromList(trackList),
                tracksNumber = trackList.size,
                albumArtUri = Utils.getAlbumArtUri(id.toLong())
            )
        }
    }
}