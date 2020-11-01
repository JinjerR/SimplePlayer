package com.jinjer.simpleplayer.data.repositories

import android.content.Context
import android.provider.MediaStore
import com.jinjer.simpleplayer.domain.data_sources.entities.AlbumEntity
import com.jinjer.simpleplayer.domain.data_sources.entities.SingerEntity
import com.jinjer.simpleplayer.domain.data_sources.entities.TrackEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// TODO: map.getOrPut

class LocalMusicSource(private val appContext: Context): IMusicSource {

    private val singers = HashMap<Int, SingerEntity>()
    private val albums = HashMap<Int, AlbumEntity>()
    private var tracks = HashMap<Int, TrackEntity>()

    private var tracksLoaded = false

    override suspend fun getTrackById(trackId: Int): TrackEntity? {
        return tracks[trackId]
    }

    override suspend fun getAlbumById(albumId: Int): AlbumEntity? {
        return albums[albumId]
    }

    override suspend fun getSingerById(singerId: Int): SingerEntity? {
        return singers[singerId]
    }

    override suspend fun searchSingerByName(query: String): List<SingerEntity>? {
        val result = mutableListOf<SingerEntity>()

        for (singerId in singers.keys) {
            val singer = getSingerById(singerId) ?: continue
            val singerName = singer.singerName ?: continue

            if (singerName.contains(query, true)) {
                result.add(singer)
            }
        }

        return result.toList()
    }

    override suspend fun searchAlbumByTitle(query: String): List<AlbumEntity>? {
        val result = mutableListOf<AlbumEntity>()

        for (albumId in albums.keys) {
            val album = getAlbumById(albumId) ?: continue
            val albumName = album.albumName ?: continue

            if (albumName.contains(query, true)) {
                result.add(album)
            }
        }

        return result
    }

    override suspend fun searchTrackByTitle(query: String): List<TrackEntity> {
        val result = mutableListOf<TrackEntity>()

        for (track in tracks.values) {
            val trackTitle = track.title ?: continue

            if (trackTitle.contains(query, true)) {
                result.add(track)
            }
        }

        return result
    }

    override suspend fun loadTracks(): List<TrackEntity>? = withContext(Dispatchers.Default) {
        if (tracksLoaded) {
            return@withContext tracks.values.toList()
        }

        loadTracksInternal()

        return@withContext tracks.values.toList()
    }

    private fun loadTracksInternal(): List<TrackEntity>? {
        val trackList = getTracksFromContentProvider()

        trackList.forEach { track ->
            tracks[track.id] = track
        }

        buildAlbums()
        buildSingers()

        tracksLoaded = true

        return tracks.values.toList()
    }

    private fun buildAlbums() {
        tracks.values.groupBy { it.albumId }.forEach { (albumId, trackList) ->
            getAlbumFromTrackList(trackList)?.let { album ->
                albums[albumId] = album
            }
        }
    }

    private fun buildSingers() {
        tracks.values.groupBy { it.artistId }.forEach { (singerId, singerTrackList) ->
            val singerName = singerTrackList.firstOrNull()?.artist
            val albumsMap = singerTrackList.groupBy { it.albumId }
            val albums = mutableListOf<AlbumEntity>()

            albumsMap.values.forEach { albumTrackList ->
                getAlbumFromTrackList(albumTrackList)?.let { album ->
                    albums.add(album)
                }
            }

            singers[singerId] = SingerEntity(
                singerId,
                singerName,
                albums)
        }
    }

    private fun getAlbumFromTrackList(list: List<TrackEntity>): AlbumEntity? {
        return list.firstOrNull()?.let { track ->
            AlbumEntity(
                track.albumId,
                track.album ?: "",
                list)
        }
    }

    private fun getTracksFromContentProvider(): List<TrackEntity> {
        val data = mutableListOf<TrackEntity>()

        val musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        // TODO: use projection, selection e.t.c for effective
        appContext.contentResolver.query(musicUri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val idIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
                val titleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
                val artistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
                val artistIdIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID)
                val albumIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)
                val albumIdIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)

                val durationIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)

                do {
                    val id: Long? = cursor.getLong(idIndex)
                    val title = cursor.getString(titleIndex)
                    val artist = cursor.getString(artistIndex)
                    val artistId = cursor.getLong(artistIdIndex)
                    val album = cursor.getString(albumIndex)
                    val albumId = cursor.getLong(albumIdIndex)
                    val duration = cursor.getLong(durationIndex)

                    val track = TrackEntity(id?.toInt() ?: -1, title, artist, artistId.toInt(), album, albumId.toInt(), duration)
                    data.add(track)
                }
                while (cursor.moveToNext())
            }
        }

        return data.toList()
    }
}