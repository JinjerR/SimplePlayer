package com.jinjer.simpleplayer.presentation.utils.extensions

import android.content.Context
import com.jinjer.simpleplayer.presentation.R

fun Context.getAppName(): String {
    return resources.getString(R.string.app_name)
}