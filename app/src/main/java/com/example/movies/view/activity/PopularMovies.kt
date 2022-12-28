package com.example.movies.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.constants.NetworkConstants
import com.example.movies.databinding.ActivityShowMoviesBinding
import com.example.movies.model.Movie
import com.example.movies.model.MovieDbClient
import com.example.movies.view.adapter.MoviesAdapter
import kotlinx.android.synthetic.main.activity_show_movies.*
import kotlinx.coroutines.launch

class PopularMovies : AppCompatActivity() {


    private lateinit var popularMoviesLayoutMgr: LinearLayoutManager
    private val moviesAdapter = MoviesAdapter { navigateToDetail(it) }
    private var currentPage = 1
    private val pageSize = 20
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (!movies_list.canScrollVertically(1)) {
                currentPage += 1
                getPopularMovies()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        popularMoviesLayoutMgr = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        super.onCreate(savedInstanceState)
        val binding = ActivityShowMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.moviesList.adapter = moviesAdapter
        title = getString(R.string.most_popular_movies)
        getPopularMovies()
        binding.moviesList.addOnScrollListener(scrollListener)
    }

    private fun getPopularMovies() {
        lifecycleScope.launch {
            val popularMovies =
                MovieDbClient.service.popularMoviesList(
                    currentPage,
                    pageSize, NetworkConstants.ES_LANGUAGE,
                    NetworkConstants.APY_KEY
                )
            moviesAdapter.appendMovies(popularMovies.results)
        }
    }

    private fun navigateToDetail(movie: Movie) {
        val intent = Intent(this, MovieDetail::class.java)
        intent.putExtra(MovieDetail.EXTRA_MOVIE, movie)
        startActivity(intent)
    }
}