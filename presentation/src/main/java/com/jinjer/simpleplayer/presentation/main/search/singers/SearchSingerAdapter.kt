package com.jinjer.simpleplayer.presentation.main.search.singers

import android.view.View
import com.jinjer.simpleplayer.presentation.R
import com.jinjer.simpleplayer.presentation.utils.recyclerview.BaseAdapter

class SearchSingerAdapter(
    itemClick: (SearchSingerPresenter) -> Unit
): BaseAdapter<SearchSingerPresenter, SearchSingerViewHolder>(DiffSearchSingers(), itemClick) {

    override var layoutId: Int = R.layout.item_search_singers

    override fun initHolder(view: View) = SearchSingerViewHolder(view)
}