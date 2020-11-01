package com.jinjer.simpleplayer.presentation.main.search.albums

import androidx.recyclerview.widget.DiffUtil
import com.jinjer.simpleplayer.presentation.models.album.Album

class DiffSearchAlbums: DiffUtil.ItemCallback<Album>() {
    override fun areContentsTheSame(
        oldItem: Album,
        newItem: Album
    ): Boolean {
        return oldItem.albumName == newItem.albumName
    }

    override fun areItemsTheSame(
        oldItem: Album,
        newItem: Album
    ): Boolean {
        return oldItem.albumId == newItem.albumId
    }
}