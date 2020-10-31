package com.jinjer.simpleplayer.presentation.main.search.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jinjer.simpleplayer.presentation.R
import com.jinjer.simpleplayer.presentation.base.BaseFragment
import com.jinjer.simpleplayer.presentation.databinding.FragmentSearchBaseBinding
import com.jinjer.simpleplayer.presentation.main.search.IOnItemCountChanged
import com.jinjer.simpleplayer.presentation.main.search.SearchType
import com.jinjer.simpleplayer.presentation.utils.custom_components.IOnSearchChangeListener
import com.jinjer.simpleplayer.presentation.utils.recyclerview.BaseAdapter
import com.jinjer.simpleplayer.presentation.utils.recyclerview.BaseViewHolder

abstract class SearchFragmentBase<P, VH: BaseViewHolder<P>>: BaseFragment(), IOnSearchChangeListener {

    private lateinit var binding: FragmentSearchBaseBinding
    private var listenerOnCountChanged: IOnItemCountChanged? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listenerOnCountChanged = parentFragment as? IOnItemCountChanged
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_base, container, false)
        binding.lifecycleOwner = this

        initRecycler()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToMainViewModel()

        searchViewModel.searchResult.observe(viewLifecycleOwner) { searchResult ->
            searchAdapter.submitList(searchResult)

            listenerOnCountChanged?.onItemCountChanged(searchResult.size, searchType)

            if (searchResult.isEmpty()) {
                val msg = if (searchViewModel.query.isEmpty()) {
                    R.string.search_no_results
                } else {
                    R.string.search_no_matches
                }

                showMessage(msg)
            } else {
                hideMessage()
            }
        }
    }

    override fun onSearchQueryChanged(query: String) {
        searchViewModel.search(query)
    }

    private fun initRecycler() {
        binding.recycler.adapter = searchAdapter
        binding.recycler.layoutManager = getLayoutManager()

        getItemDecoration()?.let {
            binding.recycler.addItemDecoration(it)
        }
    }

    private fun showMessage(@StringRes msgResId: Int) {
        binding.imgNothingFound.visibility = View.VISIBLE
        binding.txtNothingFound.visibility = View.VISIBLE
        binding.txtNothingFound.text = resources.getString(msgResId)
    }

    private fun hideMessage() {
        binding.imgNothingFound.visibility = View.INVISIBLE
        binding.txtNothingFound.visibility = View.INVISIBLE
    }

    open fun getLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    open fun getItemDecoration(): RecyclerView.ItemDecoration? = null

    open fun subscribeToMainViewModel() {
        mainViewModel.isTracksLoaded.observe(viewLifecycleOwner) { loaded ->
            if (loaded) {
                searchViewModel.onTracksLoaded()
            }
        }
    }

    abstract val searchAdapter: BaseAdapter<P, VH>

    abstract val searchViewModel: SearchViewModelBase<P>

    abstract val searchType: SearchType
}