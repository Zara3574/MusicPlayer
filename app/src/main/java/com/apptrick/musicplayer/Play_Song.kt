package com.apptrick.musicplayer
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.apptrick.musicplayer.databinding.ActivityPlaySongBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
class Play_Song : AppCompatActivity(), ServiceConnection, MediaPlayer.OnCompletionListener {
    companion object {
        var position: Int = 0
        var musicList: ArrayList<Music> = arrayListOf()
        var isPlaying: Boolean = false
        var mediaService: MusicService? = null

        @SuppressLint("StaticFieldLeak")
        lateinit var binding: ActivityPlaySongBinding
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MusicPlayer)
        binding = ActivityPlaySongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeLayout()
        var intent = Intent(this, MusicService::class.java)
        bindService(intent, this, BIND_AUTO_CREATE)
        startService(intent)
        binding.prevButton.setOnClickListener {
            println("Prev Button")
            prevNextHandler(false)
        }
        binding.nextButton.setOnClickListener {
            prevNextHandler(true)
        }
        binding.playPauseButton.setOnClickListener {
            if (isPlaying)
                pause()
            else
                play()
        }
        binding.songProgress.setOnSeekBarChangeListener(@SuppressLint("AppCompatCustomView")
        object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                if (fromUser) {
                    mediaService!!.musicPlayer!!.seekTo(progress) // Seek media player to the new position
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                mediaService!!.musicPlayer!!.pause()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                mediaService!!.musicPlayer!!.start()
            }
        }
        )
    }

    fun setLayout() {
        binding.playsongSongName.text = musicList[position].title
//        holder.duration.text=formatDuration(musicList[position].duration)
//        holder.songAlbum.text=musicList[position].des
        Glide.with(this)
            .load(musicList[position].artUri)
            .apply(RequestOptions().placeholder(R.drawable.splash_screen).centerCrop())
            .into(binding.playSongSongImg)
    }

    fun initializeLayout() {
        position = intent.getIntExtra("index", 0)
        when (intent.getStringExtra("class")) {
            "MusicAdapter" -> {
                isPlaying = true
                musicList = ArrayList<Music>()
                println(MainActivity.ListOfSongs)
                musicList.addAll(MainActivity.ListOfSongs)
                setLayout()
            }

            "MainActivity" -> {
                isPlaying = true
                musicList = ArrayList<Music>()
                musicList.addAll(MainActivity.ListOfSongs)
                musicList.shuffle()
                setLayout()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun createMediaPlayer() {
        if (mediaService!!.musicPlayer == null) mediaService!!.musicPlayer = MediaPlayer()
        mediaService!!.musicPlayer!!.reset()
        mediaService!!.musicPlayer!!.setDataSource(musicList[position].path)
        mediaService!!.musicPlayer!!.prepare()
        mediaService!!.musicPlayer!!.start()
        binding.musicStart.text =
            formatDuration(mediaService!!.musicPlayer!!.currentPosition.toLong())
        binding.musicEnd.text =
            formatDuration(mediaService!!.musicPlayer!!.duration.toLong())
        binding.playsongSongName.text = musicList[position].title
        mediaService!!.createNotification(R.drawable.notif_pause)
        binding.songProgress.progress = 0
        binding.songProgress.max = mediaService!!.musicPlayer!!.duration
        mediaService!!.musicPlayer!!.setOnCompletionListener(this)

    }
    fun play() {
        binding.playPauseButton.setIconResource(R.drawable.pause)
        isPlaying = true
        mediaService!!.musicPlayer!!.start()
    }

    fun pause() {
        binding.playPauseButton.setIconResource(R.drawable.play_solid)
        isPlaying = false
        mediaService!!.musicPlayer!!.pause()
    }

    fun prevNextHandler(increment: Boolean) {
        if (increment) {
            if (position == musicList.size - 1) position = 0
            else ++position
        } else {
            if (position == 0)
                position = musicList.size - 1
            else
                --position
        }
        setLayout()
        createMediaPlayer()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onServiceConnected(
        name: ComponentName?,
        service: IBinder?
    ) {
        val binder = service as MusicService.MyBinder
        mediaService = binder.currentService()
        createMediaPlayer()
        mediaService!!.seekbarSetup()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        mediaService = null
    }

    override fun onCompletion(mp: MediaPlayer?) {
        prevNextHandler(true)
        createMediaPlayer()
    }
}