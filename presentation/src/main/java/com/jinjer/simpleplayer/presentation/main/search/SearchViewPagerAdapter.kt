package com.jinjer.simpleplayer.presentation.main.search

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jinjer.simpleplayer.presentation.main.search.albums.SearchAlbumsFragment
import com.jinjer.simpleplayer.presentation.main.search.singers.SearchSingersFragment
import com.jinjer.simpleplayer.presentation.main.search.tracks.SearchTracksFragment

class SearchViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> SearchTracksFragment()
            1 -> SearchSingersFragment()

            else -> SearchAlbumsFragment()
        }
    }
}