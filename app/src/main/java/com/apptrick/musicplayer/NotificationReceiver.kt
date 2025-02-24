package com.apptrick.musicplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.widget.Toast
import com.apptrick.musicplayer.Play_Song.Companion.binding
import com.apptrick.musicplayer.Play_Song.Companion.mediaService
import com.apptrick.musicplayer.Play_Song.Companion.musicList
import com.apptrick.musicplayer.Play_Song.Companion.position
import kotlin.system.exitProcess
class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            "previous" -> prevNextHandler(false, context)
            "pause" -> if (Play_Song.isPlaying) pauseMusic(context) else playMusic(context)
            "next" -> prevNextHandler(true, context)
            "exit" -> {
                mediaService!!.stopSelf()
                mediaService = null
                exitProcess(1)
            }
        }
    }

    fun playMusic(context: Context?) {
        Play_Song.isPlaying = true
        Toast.makeText(context, "Play Music is Running", Toast.LENGTH_SHORT).show()
        mediaService!!.musicPlayer!!.start()
        mediaService!!.createNotification(R.drawable.notif_pause)
        Play_Song.binding.playPauseButton.setIconResource(R.drawable.pause)
    }

    fun pauseMusic(context: Context?) {
        Toast.makeText(context, "Pause Music is Running", Toast.LENGTH_SHORT).show()
        Play_Song.isPlaying = false
        mediaService!!.musicPlayer!!.pause()
        mediaService!!.createNotification(R.drawable.notif_play)
        binding.playPauseButton.setIconResource(R.drawable.play_solid)
    }

    fun prevNextHandler(increment: Boolean, context: Context?) {
        if (increment) {
            if (position == musicList.size - 1) position = 0
            else ++position
        } else {
            if (position == 0)
                position = musicList.size - 1
            else
                --position
        }
        binding.playsongSongName.text = musicList[position].title
        if (mediaService!!.musicPlayer == null) mediaService!!.musicPlayer = MediaPlayer()
        mediaService!!.musicPlayer!!.reset()
        mediaService!!.musicPlayer!!.setDataSource(musicList[position].path)
        mediaService!!.musicPlayer!!.prepare()
        mediaService!!.musicPlayer!!.start()
        mediaService!!.createNotification(R.drawable.notif_pause)
        binding.songProgress.progress = 0
        binding.songProgress.max = mediaService!!.musicPlayer!!.duration
        binding.musicStart.text =
            formatDuration(mediaService!!.musicPlayer!!.currentPosition.toLong())
        binding.musicEnd.text =
            formatDuration(mediaService!!.musicPlayer!!.duration.toLong())
         mediaService!!.musicPlayer!!.setOnCompletionListener(context as MediaPlayer.OnCompletionListener?)
    }

}