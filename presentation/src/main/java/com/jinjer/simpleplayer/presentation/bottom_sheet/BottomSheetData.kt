package com.jinjer.simpleplayer.presentation.bottom_sheet

import android.os.Parcelable
import android.view.AbsSavedState

data class BottomSheetData(
    val parentState: Parcelable,
    val state: Int): AbsSavedState(parentState)