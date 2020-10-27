package com.jinjer.simpleplayer.presentation.bottom_sheet

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior

class BottomSheetBehavior<V: View>(context: Context, attrs: AttributeSet): BottomSheetBehavior<V>(context, attrs) {

    override fun onSaveInstanceState(parent: CoordinatorLayout, child: V): Parcelable {
        return BottomSheetData(super.onSaveInstanceState(parent, child), state)
    }

    override fun onRestoreInstanceState(parent: CoordinatorLayout, child: V, bottomSheetState: Parcelable) {
        if (bottomSheetState is BottomSheetData) {
            super.onRestoreInstanceState(parent, child, bottomSheetState.parentState)
            state = bottomSheetState.state
            return
        }

        super.onRestoreInstanceState(parent, child, bottomSheetState)
    }
}