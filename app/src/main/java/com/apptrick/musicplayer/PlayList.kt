package com.apptrick.musicplayer

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.apptrick.musicplayer.databinding.ActivityMainBinding
import com.apptrick.musicplayer.databinding.ActivityPlayListBinding

class PlayList : AppCompatActivity() {
    lateinit var binding: ActivityPlayListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MusicPlayer)
        binding= ActivityPlayListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}