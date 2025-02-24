package com.apptrick.musicplayer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import com.apptrick.musicplayer.Play_Song.Companion.mediaService
import kotlinx.coroutines.Runnable

class MusicService : Service() {
    var binder = MyBinder()
    val styles = androidx.media.app.NotificationCompat.MediaStyle()
        .setShowActionsInCompactView(0, 1, 2)
    var channel_Id = "foregroundChannel"
    lateinit var runnable: Runnable
    lateinit var notification: Notification
    lateinit var notificationManager: NotificationManager
    var musicPlayer: MediaPlayer? = null
    lateinit var mediaSession: MediaSessionCompat
    override fun onCreate() {
        super.onCreate()
        createChannel()
        createNotification(R.drawable.notif_pause)
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(startId, notification)
        return START_STICKY

    }

    override fun onBind(intent: Intent?): IBinder? {
        mediaSession = MediaSessionCompat(baseContext, "MusicPlayer Session")
        return binder
    }

    inner class MyBinder : Binder() {
        fun currentService(): MusicService {
            return this@MusicService
        }
    }

    fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channel_Id, "Foreground Notifications",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createNotification(icon: Int) {
        var prevIntent = Intent(baseContext, NotificationReceiver::class.java).setAction("previous")
        var prevpendingIntent =
            PendingIntent.getBroadcast(
                baseContext,
                0,
                prevIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        var pauseIntent = Intent(baseContext, NotificationReceiver::class.java).setAction("pause")
        var pausePendingIntent =
            PendingIntent.getBroadcast(
                baseContext,
                0,
                pauseIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        var nextIntent = Intent(baseContext, NotificationReceiver::class.java).setAction("next")
        var nextPendingIntent =
            PendingIntent.getBroadcast(
                baseContext,
                0,
                nextIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        var exitIntent = Intent(baseContext, NotificationReceiver::class.java).setAction("exit")
        var exitPendingIntent =
            PendingIntent.getBroadcast(
                baseContext,
                0,
                exitIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, channel_Id)
            .setContentTitle(Play_Song.musicList[Play_Song.position].title)
            .setContentText(Play_Song.musicList[Play_Song.position].des)
            .setSmallIcon(R.drawable.splash_image)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.splash_image))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .setStyle(styles)
            .addAction(R.drawable.notif_backward, "Previous", prevpendingIntent)
            .addAction(icon, "Play", pausePendingIntent)
            .addAction(R.drawable.notif_forward, "Next", nextPendingIntent)
            .addAction(R.drawable.notif_cross, "Exit", exitPendingIntent)
        notification = builder.build()
        notificationManager.notify(1, notification)
    }

    fun seekbarSetup() {
        runnable = Runnable {
            Play_Song.binding.musicStart.text = formatDuration(musicPlayer!!.currentPosition.toLong())
            Play_Song.binding.songProgress.progress = musicPlayer!!.currentPosition
            Handler(Looper.getMainLooper()).postDelayed(runnable, 200)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable,0)

    }
}