package com.example.movies.ui.popularMovies

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
import com.example.movies.databinding.ActivityShowMoviesBinding
import com.example.movies.model.Movie
import com.example.movies.repository.MovieRepository
import com.example.movies.view.activity.MovieDetail
import com.example.movies.view.adapter.MoviesAdapter

class PopularMoviesActivity : AppCompatActivity() {


    private lateinit var popularMoviesLayoutMgr: LinearLayoutManager
    private val repository = MovieRepository()
    private val popularMoviesViewModel by viewModels<PopularMoviesViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PopularMoviesViewModel(repository) as T
            }
        }
    }
    private val moviesAdapter = MoviesAdapter { navigateToDetail(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        popularMoviesLayoutMgr = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        super.onCreate(savedInstanceState)
        val binding = ActivityShowMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.moviesList.adapter = moviesAdapter
        title = getString(R.string.most_popular_movies)
        popularMoviesViewModel.fetchPopularMovies(1)
        popularMoviesViewModel.movies.observe(this, Observer { movies ->
            if (!popularMoviesViewModel.isLoading) {
                moviesAdapter.appendMovies(movies)
                popularMoviesViewModel.isLoading = true
            }
        })
        binding.moviesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
        if (totalItemCount - lastVisibleItem <= 5 && !popularMoviesViewModel.isLoading) {
            popularMoviesViewModel.currentPage++
            popularMoviesViewModel.fetchPopularMovies(popularMoviesViewModel.currentPage)
        }
    }

}