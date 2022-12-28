package com.example.movies.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.constants.NetworkConstants
import com.example.movies.databinding.ActivityMostRatedMoviesBinding
import com.example.movies.model.Movie
import com.example.movies.model.MovieDbClient
import com.example.movies.view.adapter.MoviesAdapter
import kotlinx.android.synthetic.main.activity_most_rated_movies.*
import kotlinx.android.synthetic.main.activity_show_movies.*
import kotlinx.coroutines.launch

class MostRatedMovies : AppCompatActivity() {

    private val moviesAdapter = MoviesAdapter { navigateToDetail(it) }
    private var currentPage = 1
    private val pageSize = 20
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (!most_rated_movies_list.canScrollVertically(1)) {
                currentPage += 1
                getMostRatedMovies()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMostRatedMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mostRatedMoviesList.adapter = moviesAdapter
        title = getString(R.string.most_rated_movies)
        getMostRatedMovies()
        binding.mostRatedMoviesList.addOnScrollListener(scrollListener)
    }

    private fun getMostRatedMovies() {
        lifecycleScope.launch {
            val mostRatedMovies = MovieDbClient.service.mostRatedMoviesList(
                currentPage, pageSize, NetworkConstants.ES_LANGUAGE,
                NetworkConstants.APY_KEY
            )
            moviesAdapter.appendMovies(mostRatedMovies.results)
        }
    }

    private fun navigateToDetail(movie: Movie) {
        val intent = Intent(this, MovieDetail::class.java)
        intent.putExtra(MovieDetail.EXTRA_MOVIE, movie)
        startActivity(intent)
    }
}