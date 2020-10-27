package com.jinjer.simpleplayer.presentation.utils.extensions

import java.util.*

fun String.capitalizeWord(): String = substring(0, 1).toUpperCase(Locale.ROOT) +
        substring(1).toLowerCase(Locale.ROOT)