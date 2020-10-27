package com.jinjer.simpleplayer.presentation.main.search.albums

import androidx.recyclerview.widget.DiffUtil

class DiffSearchAlbums: DiffUtil.ItemCallback<SearchAlbumPresenter>() {
    override fun areContentsTheSame(
        oldItem: SearchAlbumPresenter,
        newItem: SearchAlbumPresenter
    ): Boolean {
        return oldItem.albumName == newItem.albumName
    }

    override fun areItemsTheSame(
        oldItem: SearchAlbumPresenter,
        newItem: SearchAlbumPresenter
    ): Boolean {
        return oldItem.albumId == newItem.albumId
    }
}