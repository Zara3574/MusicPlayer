package com.apptrick.musicplayer

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.apptrick.musicplayer.databinding.ActivityFavouriteSongsBinding

class FavouriteSongs : AppCompatActivity() {
    lateinit var binding: ActivityFavouriteSongsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MusicPlayer)
        binding= ActivityFavouriteSongsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}