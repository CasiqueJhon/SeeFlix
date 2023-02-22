package com.example.movies.ui.movieDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.movies.R
import com.example.movies.databinding.ActivityMovieDetailBinding
import com.example.movies.model.Movie
import com.example.movies.repository.MovieRepository
import com.example.movies.ui.adapter.CharactersAdapter
import com.example.movies.ui.adapter.VideosAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailActivity : AppCompatActivity() {

    @Inject
    lateinit var movieRepository: MovieRepository
    private lateinit var binding: ActivityMovieDetailBinding

    private var movie: Movie? = null
    private val movieDetailViewModel by viewModels<MovieDetailViewModel>()
    private var movieId: Int? = 0

    private val currentFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val newFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())


    companion object {
        const val EXTRA_MOVIE = "DetailActivity:title"
        const val IMAGE_URL = "https://image.tmdb.org/t/p/w780/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initDetails()
    }

    private fun initDetails() {
        movie = intent.getParcelableExtra(EXTRA_MOVIE) ?: Movie()
        if (movie != null) showMovieDetails()
        prepareAdapter()
        checkIsMovieIsFavorite()
        likeButton()
    }

    private fun showMovieDetails() {
        Glide
            .with(this)
            .load("$IMAGE_URL${movie?.backdrop_path}")
            .into(binding.imgBackdrop)
        binding.movieDescription.text = movie?.overview
        val serverDate = movie?.release_date
        val date = serverDate?.let { currentFormat.parse(it) }
        val formattedDate = date?.let { newFormat.format(it) }
        binding.movieRelease.text = formattedDate
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

    private fun likeButton() {
        movieDetailViewModel.isFavorite.observe(this) { isFavorite ->
            val likeButton = binding.btnLike
            likeButton.setIconResource(if (isFavorite == true) R.drawable.ic_favorite_full else R.drawable.ic_favorite)
            likeButton.setTag(R.string.is_liked, isFavorite)
        }

        val likeButton = binding.btnLike
        likeButton.setTag(R.string.is_liked, movieDetailViewModel.isFavorite.value)
        likeButton.setIconResource(if (likeButton.getTag(R.string.is_liked) as? Boolean == true) R.drawable.ic_favorite_full else R.drawable.ic_favorite)
        likeButton.setOnClickListener {
            val isLiked = it.getTag(R.string.is_liked) as? Boolean ?: false
            if (isLiked) {
                likeButton.setIconResource(R.drawable.ic_favorite)
                it.setTag(R.string.is_liked, false)
                Toast.makeText(this, getString(R.string.remove_movie_favorite), Toast.LENGTH_SHORT).show()
            } else {
                likeButton.setIconResource(R.drawable.ic_favorite_full)
                it.setTag(R.string.is_liked, true)
                if (movieDetailViewModel.movie.value != null) {
                    val favorite = movieDetailViewModel.movie.value!!
                    favorite.isFavorite = true
                    movieDetailViewModel.addToFavorites(favorite)
                }
                Toast.makeText(this, getString(R.string.add_favorite_movie), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkIsMovieIsFavorite() {
        movieId?.let { movieDetailViewModel.getFavoriteMovie(it) }
        movieId?.let { movieDetailViewModel.isFavoriteMovie(it) }
    }

}