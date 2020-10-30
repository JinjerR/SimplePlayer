package com.jinjer.simpleplayer.presentation.controller.service

import android.support.v4.media.session.PlaybackStateCompat

enum class PlayerState {
    ERROR,
    IDLE,
    INITIALIZED,
    PREPARING,
    PREPARED,
    STARTED,
    PAUSED,
    STOPPED,
    COMPLETED,
    @Suppress("unused")
    END,
    NONE;

    companion object {
        fun convertToPlaybackState(playerState: PlayerState): Int {
            return when(playerState) {
                ERROR -> PlaybackStateCompat.STATE_ERROR
                PREPARING -> PlaybackStateCompat.STATE_BUFFERING
                STARTED -> PlaybackStateCompat.STATE_PLAYING
                PAUSED -> PlaybackStateCompat.STATE_PAUSED
                STOPPED -> PlaybackStateCompat.STATE_STOPPED

                else -> PlaybackStateCompat.STATE_NONE
            }
        }

        @Suppress("unused")
        fun convertFromPlaybackState(playbackState: Int): PlayerState {
            return when(playbackState) {
                PlaybackStateCompat.STATE_ERROR -> ERROR
                PlaybackStateCompat.STATE_BUFFERING -> PREPARING
                PlaybackStateCompat.STATE_PLAYING -> STARTED
                PlaybackStateCompat.STATE_PAUSED -> PAUSED
                PlaybackStateCompat.STATE_STOPPED -> STOPPED

                else -> NONE
            }
        }
    }
}