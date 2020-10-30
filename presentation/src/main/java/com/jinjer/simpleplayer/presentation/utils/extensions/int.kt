package com.jinjer.simpleplayer.presentation.utils.extensions

@Suppress("unused")
fun Int.playbackStateToString():String {
    return when(this) {
        0 -> "STATE_NONE"
        1 -> "STATE_STOPPED"
        2 -> "STATE_PAUSED"
        3 -> "STATE_PLAYING"
        4 -> "STATE_FAST_FORWARDING"
        5 -> "STATE_REWINDING"
        6 -> "STATE_BUFFERING"
        7 -> "STATE_ERROR"
        8 -> "STATE_CONNECTING"
        9 -> "STATE_SKIPPING_TO_PREVIOUS"
        10 -> "STATE_SKIPPING_TO_NEXT"
        11 -> "STATE_SKIPPING_TO_QUEUE_ITEM"

        else -> "UNKNOWN_STATE"
    }
}