package com.jinjer.simpleplayer.presentation.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jinjer.simpleplayer.presentation.controller.service.QueueData
import com.jinjer.simpleplayer.presentation.main.folders.FoldersFragment
import com.jinjer.simpleplayer.presentation.main.search.MainSearchFragment
import com.jinjer.simpleplayer.presentation.main.tracks.TracksFragment

class ViewPagerAdapterMain(fm: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> TracksFragment.newInstance(null, QueueData.buildAllTracksData())
            1 -> FoldersFragment()

            else -> MainSearchFragment()
        }
    }
}