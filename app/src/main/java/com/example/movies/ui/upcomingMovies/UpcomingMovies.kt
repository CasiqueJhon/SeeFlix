package com.example.movies.ui.upcomingMovies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.constants.NetworkConstants
import com.example.movies.databinding.ActivityUpcomingMoviesBinding
import com.example.movies.model.Movie
import com.example.movies.model.MovieDbClient
import com.example.movies.view.activity.MovieDetail
import com.example.movies.view.adapter.MoviesAdapter
import kotlinx.android.synthetic.main.activity_upcoming_movies.*
import kotlinx.coroutines.launch

class UpcomingMovies : AppCompatActivity() {

    private val moviesAdapter = MoviesAdapter { navigateToDetail(it) }
    private var currentPage = 1
    private val pageSize = 20
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (!upcoming_movies_list.canScrollVertically(1)) {
                currentPage += 1
                getUpcomingMovies()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityUpcomingMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.upcomingMoviesList.adapter = moviesAdapter
        title = getString(R.string.upcoming_movies)
        getUpcomingMovies()
        binding.upcomingMoviesList.addOnScrollListener(scrollListener)
    }

    private fun getUpcomingMovies() {
        lifecycleScope.launch {
            val upcomingMovies = MovieDbClient.service.upcomingMoviesList(
                currentPage, pageSize, NetworkConstants.ES_LANGUAGE,
                NetworkConstants.APY_KEY
            )
            moviesAdapter.appendMovies(upcomingMovies.results)
        }
    }

    private fun navigateToDetail(movie: Movie) {
        val intent = Intent(this, MovieDetail::class.java)
        intent.putExtra(MovieDetail.EXTRA_MOVIE, movie)
        startActivity(intent)
    }
}