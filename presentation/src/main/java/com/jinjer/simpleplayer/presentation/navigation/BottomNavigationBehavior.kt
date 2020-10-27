package com.jinjer.simpleplayer.presentation.navigation

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.jinjer.simpleplayer.presentation.R
import com.jinjer.simpleplayer.presentation.utils.extensions.roundUpTwoDecimalPlaces

class BottomNavigationBehavior<V: View>(context: Context, attrs: AttributeSet): CoordinatorLayout.Behavior<V>(context, attrs) {

    private var playerCollapsedHeight: Float
    private var bottomSheetPercent: Float = -1f

    init {
        with(context.resources) {
            playerCollapsedHeight = getDimension(R.dimen.collapsed_player_height)
        }
    }

    override fun layoutDependsOn(parent: CoordinatorLayout, child: V, dependency: View): Boolean {
        return dependency.tag as? String == parent.context.getString(R.string.tag_fragment_player)
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: V,
        dependency: View
    ): Boolean {

        val bottomSheetMaxY = parent.height - ( playerCollapsedHeight + child.height )
        val bottomNavigationInitialY = parent.height - playerCollapsedHeight
        val bottomSheetMinY = bottomSheetMaxY / 2

        var bottomSheetCollapsedPercent = ( (dependency.y / bottomSheetMinY) - 1.0f).roundUpTwoDecimalPlaces()
        if (bottomSheetCollapsedPercent < 0f) {
            bottomSheetCollapsedPercent = 0f
        }

        if (bottomSheetPercent == bottomSheetCollapsedPercent) {
            return false
        }

        bottomSheetPercent = bottomSheetCollapsedPercent

        val bottomNavCoefficient = (1.0f - bottomSheetPercent).roundUpTwoDecimalPlaces()
        child.y = bottomNavigationInitialY + (playerCollapsedHeight * bottomNavCoefficient)

        return true
    }
}