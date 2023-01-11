package com.example.movies.ui.mostRatedMovies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.databinding.ActivityMostRatedMoviesBinding
import com.example.movies.model.Movie
import com.example.movies.repository.MovieRepository
import com.example.movies.ui.movieDetail.MovieDetail
import com.example.movies.ui.adapter.MoviesAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MostRatedMovies : AppCompatActivity() {

    private lateinit var mostRatedMoviesLayoutMgr: LinearLayoutManager
    @Inject
    lateinit var repository : MovieRepository
    private val mosRatedMoviesViewModel by viewModels<MosRatedMoviesViewModel>()
    private val moviesAdapter = MoviesAdapter { navigateToDetail(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        mostRatedMoviesLayoutMgr = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        super.onCreate(savedInstanceState)
        val binding = ActivityMostRatedMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mostRatedMoviesList.adapter = moviesAdapter
        title = getString(R.string.most_rated_movies)
        mosRatedMoviesViewModel.fetchMostRatedMovies(1)
        mosRatedMoviesViewModel.movies.observe(this) { movies ->
            if (!mosRatedMoviesViewModel.isLoading)
                moviesAdapter.appendMovies(movies)
            mosRatedMoviesViewModel.isLoading = true
        }
        binding.mostRatedMoviesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
        if (totalItemCount - lastVisibleItem <= 5 && !mosRatedMoviesViewModel.isLoading) {
            mosRatedMoviesViewModel.currentPage++
            mosRatedMoviesViewModel.fetchMostRatedMovies(mosRatedMoviesViewModel.currentPage)
        }
    }
}