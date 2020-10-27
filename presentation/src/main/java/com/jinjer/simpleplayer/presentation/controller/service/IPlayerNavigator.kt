package com.jinjer.simpleplayer.presentation.controller.service

import com.jinjer.simpleplayer.presentation.models.Track

interface IPlayerNavigator {
    fun currentTrack(): Track?

    fun nextTrack(): Track?

    fun previousTrack(): Track?

    fun setTrack(trackId: Int): Boolean

    fun setFirstTrackIfExist()

    fun playAlbum(albumId: Int): Track?

    fun playArtist(artistId: Int): Track?
}