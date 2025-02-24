package com.apptrick.musicplayer

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class ApplicationClass : Application() {
    companion object {
        val channel_Id = "channel 1"
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notChannel = NotificationChannel(
                channel_Id, "Music Payer Notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            notChannel.description="This is Music Player Notification"
            var notificationManager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notChannel)
        }
    }
}