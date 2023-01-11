package com.example.movies.ui.adapter

import android.view.LayoutInflater.*
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movies.R
import com.example.movies.databinding.CreditsItemBinding
import com.example.movies.model.Cast

class CharactersAdapter(
    var creditsList: List<Cast>
) : RecyclerView.Adapter<CharactersAdapter.CharacterViewHolder>() {

    fun setData(data: List<Cast>) {
        this.creditsList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = CreditsItemBinding.inflate(from(parent.context), parent, false)
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val characters = creditsList[position]
        holder.bind(characters)
    }

    override fun getItemCount(): Int {
        return creditsList.size
    }

    class CharacterViewHolder(
        private val binding: CreditsItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(cast: Cast) {
            Glide.with(binding.root.context)
                .load("https://image.tmdb.org/t/p/w300/${cast.profile_path}")
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_broken_image)
                .into(binding.characterThumbnail)
            binding.creditsName.text = cast.name
            binding.characterDetail.text = cast.character
        }
    }
}
