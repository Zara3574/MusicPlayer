package com.apptrick.musicplayer

import android.annotation.SuppressLint
import java.util.concurrent.TimeUnit

data class Music(
    val id: String, val title: String, val des: String,
    val duration: Long,
    val path: String,
    val artUri: String
)
@SuppressLint("DefaultLocale")
fun formatDuration(duration: Long): String
{
    val min= TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
    val sec=(TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS)-min*TimeUnit.SECONDS.convert(1,
        TimeUnit.MINUTES))
    return String.format("%02d:%02d",min,sec)
}
