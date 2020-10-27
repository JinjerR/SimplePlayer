package com.jinjer.simpleplayer.presentation.controller.service

class MediaPlayerException(what: Int = -1, extra: Int = -1, playerState: PlayerState)
    : Exception("what = $what, extra = $extra, playerState = $playerState")