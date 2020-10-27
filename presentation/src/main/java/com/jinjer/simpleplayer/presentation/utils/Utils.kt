package com.jinjer.simpleplayer.presentation.utils

import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore

object Utils {
    fun getAlbumArtUri(albumId: Long): Uri {
        val sArtworkUri = Uri.parse("content://media/external/audio/albumart")

        return ContentUris.withAppendedId(
            sArtworkUri,
            albumId)
    }

    fun getSongUri(songId: Long): Uri {
        return ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songId)
    }

    fun isFirstRow(position: Int, columnCount: Int): Boolean {
        return position < columnCount
    }

    fun isLastRow(position: Int, columnCount: Int, itemCount: Int): Boolean {
        return position >= (itemCount - columnCount)
    }
}