package com.jinjer.simpleplayer.presentation.controller.service

import android.os.Parcelable
import com.jinjer.simpleplayer.presentation.controller.service.MusicService.Companion.tagMusicControl
import com.jinjer.simpleplayer.presentation.controller.service.QueueType.*
import com.jinjer.simpleplayer.presentation.utils.ShowLog
import com.jinjer.simpleplayer.presentation.utils.ShowLog.tagTest
import kotlinx.android.parcel.Parcelize
import kotlin.collections.ArrayList

enum class QueueType {
    ALL_TRACKS,
    SINGER,
    ALBUM
}

@Parcelize
data class QueueData(
    val id: Int,
    val trackIds: List<Int>,
    val type: QueueType
): Parcelable {
    companion object {
        fun buildAllTracksData(): QueueData {
            return QueueData(-1,  emptyList(), ALL_TRACKS)
        }
        fun buildAlbumQueue(albumId: Int, trackIds: List<Int>): QueueData {
            return QueueData(albumId, trackIds, ALBUM)
        }
    }
}

// TODO: class description
class PlayerNavigator(private val trackIds: List<Int>): IPlayerNavigator {
    private val simpleName = PlayerNavigator::class.java.simpleName
    private val currentQueue = ArrayList(trackIds)

    private var currentIdx = -1

    override fun currentTrack(): Int? {
        return currentQueue.getOrNull(currentIdx) ?: run {
            ShowLog.w("$simpleName.currentTrack() queue size = ${ currentQueue.size }, currentIdx = $currentIdx", tagMusicControl)
            null
        }
    }

    override fun nextTrack(): Int? {
        if (currentQueue.isEmpty()) {
            ShowLog.w("$simpleName.nextTrack() current queue is empty", tagMusicControl)
            return null
        }

        if (++currentIdx == currentQueue.size) {
            currentIdx = 0
        }

        return currentQueue[currentIdx]
    }

    override fun previousTrack(): Int? {
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
        val idx = currentQueue.indexOfFirst { it == trackId }
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

    override fun setQueue(queueData: QueueData, initialTrackId: Int?) {
        setQueueInternal(queueData)
        initialTrackId?.let { setTrack(it) }
    }

    private fun setQueueInternal(queueData: QueueData) {
        val currentTrackIdx = currentTrack()

        val trackList = if (queueData.type == ALL_TRACKS)
            trackIds
        else
            queueData.trackIds

        currentQueue.clear()
        currentQueue.addAll(trackList)

        currentTrackIdx?.let {
            setTrack(it)
        }
    }

    companion object {
        const val keyQueueData = "key_queue_data"
    }
}