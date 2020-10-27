package com.jinjer.simpleplayer.presentation.main.player

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.jinjer.simpleplayer.presentation.base.BaseActivity
import com.jinjer.simpleplayer.presentation.base.IOnBackPressed
import com.jinjer.simpleplayer.presentation.bottom_sheet.IBottomSheetParent

abstract class PlayerActivity: BaseActivity(), IBottomSheetParent, IOnBackPressed {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    // call this method from subclasses after initialization of the view,
    // since here we are working with already initialized views
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBottomSheet()
        addPlayer()
    }

    override fun addBottomSheetCallback(callback: BottomSheetBehavior.BottomSheetCallback) {
        bottomSheetBehavior.addBottomSheetCallback(callback)
    }

    private fun setupBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(getBehaviorView()).apply {
            peekHeight = getBottomMargin()
        }
    }

    private fun addPlayer() {
        val fragment = PlayerFragment()

        supportFragmentManager
            .beginTransaction()
            .add(idFragmentPlayer, fragment, null)
            .commit()
    }

    override fun backProcessed(): Boolean {
        return if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_COLLAPSED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            true
        } else {
            false
        }
    }

    override fun expandBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    abstract fun getBehaviorView(): View

    abstract fun getBottomMargin(): Int

    abstract var idFragmentPlayer: Int
}