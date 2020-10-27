package com.jinjer.simpleplayer.presentation.main.search.singers

import androidx.recyclerview.widget.RecyclerView
import com.jinjer.simpleplayer.presentation.R
import com.jinjer.simpleplayer.presentation.main.search.SearchType
import com.jinjer.simpleplayer.presentation.main.search.base.SearchFragmentBase
import com.jinjer.simpleplayer.presentation.utils.custom_components.RecyclerItemDecoration
import com.jinjer.simpleplayer.presentation.utils.extensions.fragmentViewModel

class SearchSingersFragment: SearchFragmentBase<SearchSingerPresenter, SearchSingerViewHolder>() {

    override val adapter = SearchSingerAdapter(::onItemClick)

    override val viewModel: SearchSingersViewModel by fragmentViewModel()

    override val searchType: SearchType = SearchType.SINGERS

    override fun getItemDecoration(): RecyclerView.ItemDecoration? {
        val verticalSpace = resources.getDimension(R.dimen.space_half)
        return RecyclerItemDecoration(verticalSpace.toInt())
    }

    private fun onItemClick(data: SearchSingerPresenter) {

    }
}