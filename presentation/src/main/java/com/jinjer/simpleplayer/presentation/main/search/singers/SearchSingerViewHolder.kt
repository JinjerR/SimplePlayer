package com.jinjer.simpleplayer.presentation.main.search.singers

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.jinjer.simpleplayer.presentation.R
import com.jinjer.simpleplayer.presentation.models.singer.Singer
import com.jinjer.simpleplayer.presentation.utils.recyclerview.BaseViewHolder

class SearchSingerViewHolder(view: View): BaseViewHolder<Singer>(view) {
    private val name: AppCompatTextView = view.findViewById(R.id.txt_name)
    private val trackCount: AppCompatTextView = view.findViewById(R.id.txt_tracks_count)

    override fun bind(data: Singer) {
        name.text = data.bandName
        trackCount.text = data.trackCount.toString()
    }
}