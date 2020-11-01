package com.jinjer.simpleplayer.presentation.main.search.singers

import androidx.recyclerview.widget.DiffUtil
import com.jinjer.simpleplayer.presentation.models.singer.Singer

class DiffSearchSingers: DiffUtil.ItemCallback<Singer>() {
    override fun areContentsTheSame(
        oldItem: Singer,
        newItem: Singer
    ): Boolean {
        return oldItem.bandName == newItem.bandName
    }

    override fun areItemsTheSame(
        oldItem: Singer,
        newItem: Singer
    ): Boolean {
        return oldItem.singerId == newItem.singerId
    }
}