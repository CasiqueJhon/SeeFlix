package com.example.movies.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.databinding.VideosItemBinding
import com.example.movies.model.Videos
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.android.synthetic.main.videos_item.view.*

class VideosAdapter(
    var videosList: List<Videos>
): RecyclerView.Adapter<VideosAdapter.VideosViewHolder>() {

    fun setData(data: List<Videos>) {
        this.videosList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideosViewHolder {
        val binding = VideosItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideosViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideosViewHolder, position: Int) {
        val videos = videosList[position]
        holder.bind(videos)
    }

    override fun getItemCount(): Int = videosList.size

    class VideosViewHolder(
        private val binding: VideosItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private var player : YouTubePlayer? = null

        fun bind(videos: Videos) {
            binding.videosItem
            player?.loadVideo(videos.key, 0f)
        }
    }
}