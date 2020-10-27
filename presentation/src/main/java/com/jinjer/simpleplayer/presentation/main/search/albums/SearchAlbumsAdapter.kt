package com.jinjer.simpleplayer.presentation.main.search.albums

import android.view.View
import com.jinjer.simpleplayer.presentation.R
import com.jinjer.simpleplayer.presentation.main.search.ItemAlbumType
import com.jinjer.simpleplayer.presentation.main.search.ItemAlbumType.HORIZONTAL
import com.jinjer.simpleplayer.presentation.utils.recyclerview.BaseAdapter

class SearchAlbumsAdapter(
    itemType: ItemAlbumType,
    itemClick: (SearchAlbumPresenter) -> Unit
): BaseAdapter<SearchAlbumPresenter, SearchAlbumsViewHolder>(DiffSearchAlbums(), itemClick) {

    override var layoutId: Int =
        if (itemType == HORIZONTAL) R.layout.item_search_albums_horizontal
        else R.layout.item_album_vertical

    override fun initHolder(view: View) = SearchAlbumsViewHolder(view)
}