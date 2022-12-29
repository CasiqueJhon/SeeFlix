package com.example.movies.ui.upcomingMovies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.databinding.ActivityUpcomingMoviesBinding
import com.example.movies.model.Movie
import com.example.movies.repository.MovieRepository
import com.example.movies.ui.MovieDetail
import com.example.movies.ui.adapter.MoviesAdapter

class UpcomingMovies : AppCompatActivity() {

    private lateinit var upcomingMoviesLayoutMgr: LinearLayoutManager
    private val repository = MovieRepository()
    private val upcomingMoviesViewModel by viewModels<UpcomingMoviesViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return UpcomingMoviesViewModel(repository) as T
            }
        }
    }
    private val moviesAdapter = MoviesAdapter { navigateToDetail(it) }


    override fun onCreate(savedInstanceState: Bundle?) {
        upcomingMoviesLayoutMgr = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        super.onCreate(savedInstanceState)
        val binding = ActivityUpcomingMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.upcomingMoviesList.adapter = moviesAdapter
        title = getString(R.string.upcoming_movies)
        upcomingMoviesViewModel.fetchUpcomingMovies(1)
        upcomingMoviesViewModel.movies.observe(this, Observer { movies ->
            if (!upcomingMoviesViewModel.isLoading) {
                moviesAdapter.appendMovies(movies)
                upcomingMoviesViewModel.isLoading = true
            }
        })
        binding.upcomingMoviesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                handleOnScrolled(recyclerView, dx, dy)
            }
        })
    }

    private fun navigateToDetail(movie: Movie) {
        val intent = Intent(this, MovieDetail::class.java)
        intent.putExtra(MovieDetail.EXTRA_MOVIE, movie)
        startActivity(intent)
    }

    private fun handleOnScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
        val totalItemCount = layoutManager.itemCount
        if (totalItemCount - lastVisibleItem <= 5 && !upcomingMoviesViewModel.isLoading) {
            upcomingMoviesViewModel.currentPage++
            upcomingMoviesViewModel.fetchUpcomingMovies(upcomingMoviesViewModel.currentPage)
        }
    }
}