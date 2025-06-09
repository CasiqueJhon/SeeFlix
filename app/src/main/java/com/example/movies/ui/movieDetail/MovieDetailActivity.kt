package com.example.movies.ui.movieDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
import kotlinx.coroutines.launch
import androidx.lifecycle.Lifecycle
import com.example.movies.db.Favorite

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
        setupFavoriteButton()
        movie?.id?.let { movieDetailViewModel.checkFavoriteStatus(it) }
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

    private fun setupFavoriteButton() {
        val likeButton = binding.btnLike

        // Observar cambios en el estado de favoritos
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                movieDetailViewModel.favoriteState.collect { state ->
                    when (state) {
                        is FavoriteState.Initial -> {
                            // Estado inicial, no hacer nada
                        }
                        is FavoriteState.Loading -> {
                            // Opcional: Mostrar loading
                            likeButton.isEnabled = false
                        }
                        is FavoriteState.Success -> {
                            likeButton.isEnabled = true
                            updateFavoriteButtonUI(state.isFavorite)
                        }
                        is FavoriteState.Error -> {
                            likeButton.isEnabled = true
                            Toast.makeText(
                                this@MovieDetailActivity,
                                state.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        // Configurar click listener
        likeButton.setOnClickListener {
            movie?.let { currentMovie ->
                val favorite = Favorite(
                    movieId = currentMovie.id,
                    title = currentMovie.title,
                    overview = currentMovie.overview,
                    posterPath = currentMovie.thumbnail,
                    backdropPath = currentMovie.backdrop_path,
                    releaseDate = currentMovie.release_date,
                    voteAverage = currentMovie.vote_average
                )
                movieDetailViewModel.toggleFavorite(currentMovie.id, favorite)
            }
        }
    }

    private fun updateFavoriteButtonUI(isFavorite: Boolean) {
        val likeButton = binding.btnLike
        likeButton.setIconResource(
            if (isFavorite) R.drawable.ic_favorite_full 
            else R.drawable.ic_favorite
        )
        
        // Actualizar texto del botón
        val textRes = if (isFavorite) {
            R.string.remove_movie_favorite
        } else {
            R.string.add_favorite_movie
        }
        likeButton.text = getString(textRes)
    }

}