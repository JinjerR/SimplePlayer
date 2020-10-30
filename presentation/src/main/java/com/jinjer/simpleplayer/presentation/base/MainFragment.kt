package com.jinjer.simpleplayer.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener
import com.jinjer.simpleplayer.presentation.R
import com.jinjer.simpleplayer.presentation.databinding.FragmentMainBinding
import com.jinjer.simpleplayer.presentation.main.player.PlayerFragmentBase
import com.jinjer.simpleplayer.presentation.navigation.ViewPagerAdapterMain

class MainFragment: PlayerFragmentBase(), OnNavigationItemSelectedListener {
    private lateinit var mainBinding: FragmentMainBinding

    override var idFragmentPlayer: Int = R.id.fragment_player

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return mainBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainBinding.bottomNavigation.setOnNavigationItemSelectedListener(this)

        setupViewPager()
    }

    override fun getBehaviorView(): View = mainBinding.fragmentPlayer

    override fun getBottomMargin(): Int {
        val bottomNavHeight = resources.getDimensionPixelSize(R.dimen.bottom_navigation_height)
        val collapsedPlayerHeight = resources.getDimensionPixelSize(R.dimen.collapsed_player_height)

        return bottomNavHeight + collapsedPlayerHeight
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val selectedIndex =  when(item.itemId) {
            R.id.tracks -> 0
            R.id.folders -> 1
            R.id.search -> 2

            else -> 3
        }

        mainBinding.viewPager.setCurrentItem(selectedIndex, false)

        return true
    }

    private fun setupViewPager() {
        val viewpagerAdapter = ViewPagerAdapterMain(childFragmentManager, lifecycle)

        with(mainBinding.viewPager) {
            adapter = viewpagerAdapter
            (layoutParams as ViewGroup.MarginLayoutParams).bottomMargin = getBottomMargin()
            offscreenPageLimit = 4
            isUserInputEnabled = false
        }
    }
}