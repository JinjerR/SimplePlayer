package com.jinjer.simpleplayer.presentation.utils.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.media.session.MediaButtonReceiver
import com.jinjer.simpleplayer.presentation.R
import androidx.media.app.NotificationCompat as NotificationMediaCompat

class NotifyManager(private val appContext: Context): INotifyManager {

    private val notifyId: Int = 1132
    private val idNotificationChannel = "player_notification_id"
    private val channelName = "player notifications"

    private val notificationManager =
        appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override fun createChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.notificationChannels.count { it.id == idNotificationChannel } == 0) {
                val channel = NotificationChannel(
                    idNotificationChannel,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    enableVibration(false)
                }
                notificationManager.createNotificationChannel(channel)
            }
        }
    }

    override fun getNotificationId(): Int = notifyId

    override fun getNotification(notifyInfo: NotificationInfo): Notification {
        val playPauseAction = if (notifyInfo.isPlaying) {
            NotificationCompat.Action(
                R.drawable.icon_pause,
                "title pause",
                getPendingIntent(PlaybackStateCompat.ACTION_PAUSE))
        } else {
            NotificationCompat.Action(
                R.drawable.icon_play,
                "title play",
                getPendingIntent(PlaybackStateCompat.ACTION_PLAY))
        }

        val skipToPreviousAction = NotificationCompat.Action(
            R.drawable.icon_rewind_back,
            "title skip to previous",
            getPendingIntent(PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS))

        val skipToNextAction = NotificationCompat.Action(
            R.drawable.icon_rewind_forward,
            "title skip to next",
            getPendingIntent(PlaybackStateCompat.ACTION_SKIP_TO_NEXT))

        return NotificationCompat.Builder(appContext, idNotificationChannel).apply {
            setContentTitle(notifyInfo.trackName)
            setContentText(notifyInfo.bandName)
            setSmallIcon(R.drawable.icon_favourite)
            setLargeIcon(notifyInfo.albumImage)
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            priority = NotificationCompat.PRIORITY_HIGH
            setStyle(NotificationMediaCompat.MediaStyle().setMediaSession(notifyInfo.token))
            addAction(skipToPreviousAction)
            addAction(playPauseAction)
            addAction(skipToNextAction)
        }.build()
    }

    override fun updateNotification(notifyInfo: NotificationInfo) {
        notificationManager.notify(notifyId, getNotification(notifyInfo))
    }

    private fun getPendingIntent(action: Long): PendingIntent {
        return MediaButtonReceiver.buildMediaButtonPendingIntent(appContext, action)
    }
}