package com.apptrick.musicplayer
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apptrick.musicplayer.databinding.SongItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
class MusicAdapter(val context: Context,val musicList: ArrayList<Music>): RecyclerView.Adapter<MusicAdapter.MyHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyHolder {

        return MyHolder(SongItemBinding.inflate(LayoutInflater.from(context),parent,false))
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: MyHolder,
        position: Int
    ) {
        holder.title.text=musicList[position].title
        holder.duration.text=formatDuration(musicList[position].duration)
        holder.songAlbum.text=musicList[position].des
        Glide.with(context)
            .load(musicList[position].artUri)
            .apply(RequestOptions().placeholder(R.drawable.splash_screen).centerCrop())
            .into(holder.image)
        holder.root.setOnClickListener{
            val intent= Intent(context, Play_Song::class.java)
            intent.putExtra("index",position)
            intent.putExtra("class","MusicAdapter")
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return musicList.size
    }
    inner class MyHolder(binding: SongItemBinding): RecyclerView.ViewHolder(binding.root){
        val title=binding.songname
        val songAlbum=binding.songAlbum
        val duration=binding.songDuration
        val image=binding.songImg
        val root =binding.root
    }
}