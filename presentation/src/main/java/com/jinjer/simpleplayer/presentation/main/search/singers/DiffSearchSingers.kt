package com.jinjer.simpleplayer.presentation.main.search.singers

import androidx.recyclerview.widget.DiffUtil

class DiffSearchSingers: DiffUtil.ItemCallback<SearchSingerPresenter>() {
    override fun areContentsTheSame(
        oldItem: SearchSingerPresenter,
        newItem: SearchSingerPresenter
    ): Boolean {
        return oldItem.bandName == newItem.bandName
    }

    override fun areItemsTheSame(
        oldItem: SearchSingerPresenter,
        newItem: SearchSingerPresenter
    ): Boolean {
        return oldItem.singerId == newItem.singerId
    }
}