package com.jinjer.simpleplayer.presentation.main.search

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.jinjer.simpleplayer.presentation.main.search.albums.SearchAlbumsFragment
import com.jinjer.simpleplayer.presentation.main.search.singers.SearchSingersFragment
import com.jinjer.simpleplayer.presentation.main.search.tracks.SearchTracksFragment

class SearchViewPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> SearchTracksFragment()
            1 -> SearchSingersFragment()

            else -> SearchAlbumsFragment()
        }
    }

    override fun getCount(): Int = 3
}