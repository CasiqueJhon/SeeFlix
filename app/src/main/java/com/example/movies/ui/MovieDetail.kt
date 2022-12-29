package com.example.movies.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.movies.constants.NetworkConstants
import com.example.movies.databinding.ActivityMovieDetailBinding
import com.example.movies.model.Movie
import com.example.movies.model.MovieDbClient
import com.example.movies.ui.adapter.CharactersAdapter
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.coroutines.launch

class MovieDetail : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailBinding
    private val charactersAdapter = CharactersAdapter(emptyList())
    private var movieId : Int? = 0

    companion object {
        const val EXTRA_MOVIE = "DetailActivity:title"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        findViews()
        getMovieCredits()
        prepareAdapter()
    }

    private fun findViews() {
        val movieDetail = intent.getParcelableExtra<Movie>(EXTRA_MOVIE)
        title = movieDetail?.title
        Glide
            .with(this)
            .load("https://image.tmdb.org/t/p/w780/${movieDetail?.backdrop_path}")
            .into(binding.imgBackdrop)
        binding.movieDescription.text = movieDetail?.overview
        binding.movieRelease.text = movieDetail?.release_date
        binding.movieViews.text = movieDetail?.popularity.toString()
        binding.movieVotes.text = movieDetail?.vote_average.toString()
        movieId = movieDetail?.id
        Log.d("TAG", "movie id $movieId")
    }


    private fun getMovieCredits() {
        lifecycleScope.launch {
            val creditsList = MovieDbClient.service.creditsList(movieId, NetworkConstants.APY_KEY)
            charactersAdapter.creditsList = creditsList.cast
            charactersAdapter.notifyDataSetChanged()
        }
    }

    private fun prepareAdapter() {
        binding.creditsList.adapter = charactersAdapter
        creditsList.layoutManager = LinearLayoutManager(this@MovieDetail, LinearLayoutManager.HORIZONTAL, false)
    }
}