package com.jinjer.simpleplayer.presentation.utils.extensions

import kotlin.math.round

fun Float.roundUpTwoDecimalPlaces(): Float {
    return round(this * 100) / 100
}