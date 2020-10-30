package com.jinjer.simpleplayer.presentation.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.jinjer.simpleplayer.presentation.controller.service.QueueData
import com.jinjer.simpleplayer.presentation.main.folders.FoldersFragment
import com.jinjer.simpleplayer.presentation.main.search.MainSearchFragment
import com.jinjer.simpleplayer.presentation.main.tracks.TracksFragment

class ViewPagerAdapterMain(fm: FragmentManager): FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> TracksFragment.newInstance(null, QueueData.buildAllTracksData())
            1 -> FoldersFragment()

            else -> MainSearchFragment()
        }
    }

    override fun getCount(): Int = 3
}