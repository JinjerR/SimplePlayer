package com.jinjer.simpleplayer.presentation.utils

import java.util.*

object TimeUtils {
    private const val separator = ":"

    fun getFormattedSongDurationFromMillis(millis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.time = Date(millis)

        val minutes = calendar.get(Calendar.MINUTE)
        val seconds = calendar.get(Calendar.SECOND)

        val minutesStr = if (minutes < 10) "0$minutes" else minutes.toString()
        val secondsStr = if (seconds < 10) "0$seconds" else seconds.toString()

        return "$minutesStr$separator$secondsStr"
    }

    fun getFormattedSongDurationFromSeconds(seconds: Int): String {
        return getFormattedSongDurationFromMillis(seconds * 1000L)
    }
}