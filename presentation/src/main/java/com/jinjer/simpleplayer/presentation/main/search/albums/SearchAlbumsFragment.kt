package com.jinjer.simpleplayer.presentation.main.search.albums

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jinjer.simpleplayer.presentation.R
import com.jinjer.simpleplayer.presentation.models.album.Album
import com.jinjer.simpleplayer.presentation.main.search.ItemAlbumType
import com.jinjer.simpleplayer.presentation.main.search.SearchType
import com.jinjer.simpleplayer.presentation.main.search.base.SearchFragmentBase
import com.jinjer.simpleplayer.presentation.utils.custom_components.RecyclerItemDecoration
import com.jinjer.simpleplayer.presentation.utils.extensions.fragmentViewModel

class SearchAlbumsFragment: SearchFragmentBase<Album, SearchAlbumsViewHolder>() {

    override var searchAdapter: SearchAlbumsAdapter =
        SearchAlbumsAdapter(ItemAlbumType.VERTICAL, ::onItemClick)

    override val searchViewModel: SearchAlbumsViewModel by fragmentViewModel()

    override val searchType: SearchType = SearchType.ALBUMS

    override fun getLayoutManager(): RecyclerView.LayoutManager =
        GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)

    override fun getItemDecoration(): RecyclerView.ItemDecoration? {
        val spaceVertical = resources.getDimension(R.dimen.space_double)
        val spaceHorizontal = resources.getDimension(R.dimen.space)
        return RecyclerItemDecoration(spaceVertical.toInt(), spaceHorizontal.toInt())
    }

    private fun onItemClick(data: Album) {
        mainController?.showAlbumDetails(data.albumId)
    }
}