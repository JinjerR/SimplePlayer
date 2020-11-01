package com.jinjer.simpleplayer.presentation.models.singer

import com.jinjer.simpleplayer.domain.data_sources.entities.SingerEntity
import com.jinjer.simpleplayer.domain.utils.Mapper
import com.jinjer.simpleplayer.presentation.models.album.Album
import com.jinjer.simpleplayer.presentation.models.album.AlbumMapper
import com.jinjer.simpleplayer.presentation.models.track.Track
import com.jinjer.simpleplayer.presentation.models.track.TrackMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SingerDetailsMapper(
    private val trackMapper: TrackMapper,
    private val albumMapper: AlbumMapper
): Mapper<SingerEntity, SingerDetails>() {

    override suspend fun from(element: SingerEntity): SingerDetails = withContext(Dispatchers.Default) {
        val name = element.singerName ?: ""
        val trackList = mutableListOf<Track>()
        val albumList = mutableListOf<Album>()

        element.albums?.map { album ->
            albumList.add(albumMapper.from(album))

            album.tracks?.let { tracks ->
                trackList += trackMapper.fromList(tracks)
            }
        }

        SingerDetails(
            element.singerId,
            name,
            trackList,
            trackList.size,
            albumList,
            albumList.size
        )
    }
}