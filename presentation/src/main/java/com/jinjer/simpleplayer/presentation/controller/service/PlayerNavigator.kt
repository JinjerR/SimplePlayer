package com.jinjer.simpleplayer.presentation.controller.service

import com.jinjer.simpleplayer.presentation.models.Track
import com.jinjer.simpleplayer.presentation.controller.service.MusicService.Companion.tagMusicControl
import com.jinjer.simpleplayer.presentation.utils.ShowLog
import kotlin.collections.ArrayList

class PlayerNavigator(private val tracks: List<Track>): IPlayerNavigator {
    private val simpleName = PlayerNavigator::class.java.simpleName
    private val currentQueue = ArrayList(tracks)

    private var currentIdx = -1

    override fun currentTrack(): Track? {
        return currentQueue.getOrNull(currentIdx) ?: run {
            ShowLog.w("$simpleName.currentTrack() queue size = ${ currentQueue.size }, currentIdx = $currentIdx", tagMusicControl)
            null
        }
    }

    override fun nextTrack(): Track? {
        if (currentQueue.isEmpty()) {
            ShowLog.w("$simpleName.nextTrack() current queue is empty", tagMusicControl)
            return null
        }

        if (++currentIdx == currentQueue.size) {
            currentIdx = 0
        }

        return currentQueue[currentIdx]
    }

    override fun previousTrack(): Track? {
        if (currentQueue.isEmpty()) {
            ShowLog.w("$simpleName.previousTrack() current queue is empty", tagMusicControl)
            return null
        }

        if (--currentIdx < 0) {
            currentIdx = currentQueue.lastIndex
        }

        return currentQueue[currentIdx]
    }

    override fun setTrack(trackId: Int): Boolean {
        val idx = currentQueue.indexOfFirst { it.id == trackId }
        return if (idx == -1) {
            ShowLog.w("$simpleName.setTrack() track with id = $trackId was not found in the current queue", tagMusicControl)
            false
        } else {
            currentIdx = idx
            true
        }
    }

    override fun setFirstTrackIfExist() {
        if (currentQueue.isNotEmpty()) {
            currentIdx = 0
        }
    }

    override fun playAlbum(albumId: Int): Track? {
        return tracks.first()
    }

    override fun playArtist(artistId: Int): Track? {
        return tracks.first()
    }

//    private fun buildBrowseTree() {
//        var artistId: Int
//        var artistMap: HashMap<Int, MutableList<Track>>
//        var albumId: Int
//
//        for (track in tracks) {
//            artistId = track.artistId
//            albumId = track.albumId
//
//            if (trackMap.containsKey(artistId).not()) {
//                trackMap[artistId] = HashMap()
//            }
//
//            artistMap = trackMap[artistId]!!
//
//            if (artistMap.containsKey(albumId).not()) {
//                artistMap[albumId] = mutableListOf()
//            }
//
//            artistMap[albumId]?.add(track)
//        }
//    }
}