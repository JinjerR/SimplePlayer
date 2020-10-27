package com.jinjer.simpleplayer.presentation.bottom_sheet

import com.google.android.material.bottomsheet.BottomSheetBehavior

interface IBottomSheetParent {
    fun addBottomSheetCallback(callback: BottomSheetBehavior.BottomSheetCallback)
    fun expandBottomSheet()
}