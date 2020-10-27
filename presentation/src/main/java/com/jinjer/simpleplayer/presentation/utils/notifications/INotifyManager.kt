package com.jinjer.simpleplayer.presentation.utils.notifications

import android.app.Notification

interface INotifyManager {
    fun createChannels()

    fun getNotification(notifyInfo: NotificationInfo): Notification

    fun updateNotification(notifyInfo: NotificationInfo)

    fun getNotificationId(): Int
}