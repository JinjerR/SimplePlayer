package com.jinjer.simpleplayer.presentation.main.player

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.jinjer.simpleplayer.presentation.base.BaseFragment
import com.jinjer.simpleplayer.presentation.base.IOnBackPressed
import com.jinjer.simpleplayer.presentation.bottom_sheet.IBottomSheetParent

/** contains logic for working with [BottomSheetBehavior].
 * Needed by those who contain in their [NowPlayingFragment]
 **/
abstract class PlayerFragmentBase: BaseFragment(), IBottomSheetParent, IOnBackPressed {
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        val fragment = NowPlayingFragment()

        childFragmentManager
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