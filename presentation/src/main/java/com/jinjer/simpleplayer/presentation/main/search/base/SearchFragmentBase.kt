package com.jinjer.simpleplayer.presentation.main.search.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        subscribeToViewModel()

        viewModel.searchResult.observe(viewLifecycleOwner) { searchResult ->
            adapter.submitList(searchResult)

            listenerOnCountChanged?.onItemCountChanged(searchResult.size, searchType)

            if (searchResult.isEmpty()) {
                showNothingFound()
            } else {
                hideNothingFound()
            }
        }
    }

    override fun onSearchQueryChanged(query: String) {
        viewModel.search(query)
    }

    private fun initRecycler() {
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = getLayoutManager()

        getItemDecoration()?.let {
            binding.recycler.addItemDecoration(it)
        }
    }

    private fun showNothingFound() {
        binding.imgNothingFound.visibility = View.VISIBLE
        binding.txtNothingFound.visibility = View.VISIBLE
    }

    private fun hideNothingFound() {
        binding.imgNothingFound.visibility = View.INVISIBLE
        binding.txtNothingFound.visibility = View.INVISIBLE
    }

    abstract val adapter: BaseAdapter<P, VH>

    abstract val viewModel: SearchViewModelBase<P>

    abstract val searchType: SearchType

    open fun getLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    open fun getItemDecoration(): RecyclerView.ItemDecoration? = null

    open fun subscribeToViewModel() { }
}