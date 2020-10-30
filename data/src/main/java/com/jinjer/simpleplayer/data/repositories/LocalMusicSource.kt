package com.jinjer.simpleplayer.data.repositories

import android.annotation.SuppressLint
import android.content.Context
import android.provider.MediaStore
import com.jinjer.simpleplayer.domain.models.TrackDomain

class LocalMusicSource(private val appContext: Context): IMusicSource {
    @SuppressLint("InlinedApi")
    override suspend fun getTracks(): List<TrackDomain> {
        val data = mutableListOf<TrackDomain>()

        val musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        // TODO: use projection, selection e.t.c for effective
        appContext.contentResolver.query(musicUri, null, null, null, null).use { cursor ->
            if (cursor?.moveToFirst() == true) {
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

                    val track = TrackDomain(id?.toInt() ?: -1, title, artist, artistId.toInt(), album, albumId.toInt(), duration)
                    data.add(track)
                }
                while (cursor.moveToNext())
            }
        }

        return data.toList()
    }
}