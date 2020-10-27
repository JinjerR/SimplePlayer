@file:Suppress("unused")

package com.jinjer.simpleplayer.presentation.utils

import android.util.Log
import com.jinjer.simpleplayer.presentation.BuildConfig

object ShowLog {
    const val tagTest = "tag_test"

    private val debug = BuildConfig.DEBUG
    private const val defaultTag = "tag_simple_player"

    fun i(msg: String?, tag: String = defaultTag) {
        if (debug) {
            Log.i(tag, msg ?: "")
        }
    }

    fun e(msg: String?, tag: String = defaultTag) {
        if (debug) {
            Log.e(tag, msg ?: "")
        }
    }

    fun w(msg: String?, tag: String = defaultTag) {
        if (debug) {
            Log.w(tag, msg ?: "")
        }
    }

    fun wtf(msg: String?, tag: String = defaultTag) {
        if (debug) {
            Log.wtf(tag, msg ?: "")
        }
    }
}