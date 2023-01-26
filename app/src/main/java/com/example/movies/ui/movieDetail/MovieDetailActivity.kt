package com.example.movies.ui.movieDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.movies.databinding.ActivityMovieDetailBinding
import com.example.movies.model.Movie
import com.example.movies.repository.MovieRepository
import com.example.movies.ui.adapter.CharactersAdapter
import com.example.movies.ui.adapter.VideosAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailActivity : AppCompatActivity() {

    @Inject
    lateinit var movieRepository: MovieRepository
    private lateinit var binding: ActivityMovieDetailBinding

    private var movie: Movie? = null
    private val movieDetailViewModel by viewModels<MovieDetailViewModel>()
    private var movieId: Int? = 0

    companion object {
        const val EXTRA_MOVIE = "DetailActivity:title"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        movie = intent.getParcelableExtra(EXTRA_MOVIE) ?: Movie()
        if (movie != null) showMovieDetails()
        prepareAdapter()

    }

    private fun showMovieDetails() {
        title = movie?.title
        Glide
            .with(this)
            .load("https://image.tmdb.org/t/p/w780/${movie?.backdrop_path}")
            .into(binding.imgBackdrop)
        binding.movieDescription.text = movie?.overview
        binding.movieRelease.text = movie?.release_date
        binding.movieViews.text = movie?.popularity.toString()
        binding.movieVotes.text = movie?.vote_average.toString()
        movieId = movie?.id
        Log.d("TAG", "movie id $movieId")
    }

    private fun prepareAdapter() {
        val charactersAdapter = CharactersAdapter(movieDetailViewModel.characters.value ?: emptyList())
        val videosAdapter = VideosAdapter(movieDetailViewModel.videos.value ?: emptyList())
        binding.castList.adapter = charactersAdapter
        movieDetailViewModel.fetchMovieCredits(movieId)
        movieDetailViewModel.characters.observe(this, Observer { data ->
            charactersAdapter.setData(data)
            charactersAdapter.notifyDataSetChanged()
        })
        binding.castList.layoutManager = LinearLayoutManager(
            this@MovieDetailActivity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.videoList.adapter = videosAdapter
        movieDetailViewModel.fetchMovieVideos(movieId)
        movieDetailViewModel.videos.observe(this, Observer { videoData ->
            videosAdapter.setData(videoData)
            videosAdapter.notifyDataSetChanged()
        })
        binding.videoList.layoutManager = LinearLayoutManager(
            this@MovieDetailActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
    }
}