package com.example.movies.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movies.databinding.MovieItemBinding
import com.example.movies.model.Movie

class MoviesAdapter(
    private val movieClickListener: (Movie) -> Unit
) : RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    internal var movieList = mutableListOf<Movie>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movieList[position]
        holder.bind(movie)
        holder.itemView.setOnClickListener { movieClickListener(movie) }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }


    class ViewHolder(private val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.movieTitle.text = movie.title
            Glide
                .with(binding.root.context)
                .load("https://image.tmdb.org/t/p/w500/${movie.thumbnail}")
                .into(binding.thumbnail)
            binding.thumbnail.ratio = 1.5f
        }
    }

    fun appendMovies(movies: List<Movie>) {
        val oldSize = movieList.size
        this.movieList.addAll(movies)
        notifyItemRangeInserted(
            oldSize, movieList.size
        )
    }
}