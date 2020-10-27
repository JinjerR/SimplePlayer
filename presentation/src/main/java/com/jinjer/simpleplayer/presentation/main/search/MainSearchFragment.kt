package com.jinjer.simpleplayer.presentation.main.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.jinjer.simpleplayer.presentation.R
import com.jinjer.simpleplayer.presentation.base.BaseFragment
import com.jinjer.simpleplayer.presentation.databinding.FragmentSearchMainBinding
import com.jinjer.simpleplayer.presentation.utils.custom_components.IOnSearchChangeListener

class MainSearchFragment : BaseFragment(), IOnSearchChangeListener, IOnItemCountChanged {

    private lateinit var binding: FragmentSearchMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_main, container, false)
        binding.lifecycleOwner = this

        initViewPager()
        binding.searchBar.addSearchChangeListener(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchNavTracks.setOnClickListener(::onClickSearchNavItem)
        binding.searchNavSingers.setOnClickListener(::onClickSearchNavItem)
        binding.searchNavAlbums.setOnClickListener(::onClickSearchNavItem)

        binding.searchNavTracks.isActivated = true
    }

    override fun onResume() {
        super.onResume()
        binding.searchBar.isActivated = true
    }

    override fun onPause() {
        super.onPause()
        binding.searchBar.isActivated = false
    }

    private fun onClickSearchNavItem(view: View) {
        binding.searchBar.isActivated = false

        with(binding) {
            when (view.id) {
                R.id.search_nav_tracks -> {
                    searchNavSingers.isActivated = false
                    searchNavAlbums.isActivated = false
                    searchNavTracks.isActivated = true
                    viewPager.currentItem = 0
                }
                R.id.search_nav_singers -> {
                    searchNavSingers.isActivated = true
                    searchNavAlbums.isActivated = false
                    searchNavTracks.isActivated = false
                    viewPager.currentItem = 1
                }
                R.id.search_nav_albums -> {
                    searchNavSingers.isActivated = false
                    searchNavAlbums.isActivated = true
                    searchNavTracks.isActivated = false
                    viewPager.currentItem = 2
                }
            }
        }
    }

    private fun initViewPager() {
        val adapter = SearchViewPagerAdapter(childFragmentManager)
        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 3
    }

    override fun onSearchQueryChanged(newText: String) {
        for (fragment in childFragmentManager.fragments) {
            if (fragment is IOnSearchChangeListener) {
                fragment.onSearchQueryChanged(newText)
            }
        }
    }

    override fun onItemCountChanged(itemCount: Int, type: SearchType) {
        val searchComponent = when(type) {
            SearchType.TRACKS -> binding.searchNavTracks
            SearchType.SINGERS -> binding.searchNavSingers
            else -> binding.searchNavAlbums
        }

        searchComponent.setCount(itemCount)
    }
}