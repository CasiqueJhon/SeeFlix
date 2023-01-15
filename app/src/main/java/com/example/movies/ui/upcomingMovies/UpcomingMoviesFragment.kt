package com.example.movies.ui.upcomingMovies

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.constants.ErrorConstants
import com.example.movies.databinding.FragmentMostRatedMoviesBinding
import com.example.movies.databinding.FragmentUpcomingMoviesBinding
import com.example.movies.model.Movie
import com.example.movies.repository.MovieRepository
import com.example.movies.ui.adapter.MoviesAdapter
import com.example.movies.ui.movieDetail.MovieDetail
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UpcomingMoviesFragment : Fragment() {

    @Inject
    lateinit var repository: MovieRepository
    private lateinit var gridLayoutManager: GridLayoutManager

    private var _binding: FragmentUpcomingMoviesBinding? = null
    private val binding: FragmentUpcomingMoviesBinding
        get() = _binding ?: throw Exception(ErrorConstants.fragmentError)

    private val upcomingMoviesViewModel by viewModels<UpcomingMoviesViewModel>()
    private val moviesAdapter = MoviesAdapter { navigateToDetail(it) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gridLayoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.upcomingMoviesList.layoutManager = gridLayoutManager
        binding.upcomingMoviesList.adapter = moviesAdapter

        upcomingMoviesViewModel.fetchUpcomingMovies(1)
        upcomingMoviesViewModel.movies.observe(viewLifecycleOwner) { movies ->
            if (!upcomingMoviesViewModel.isLoading) {
                moviesAdapter.appendMovies(movies)
                upcomingMoviesViewModel.isLoading = true
            }
        }
        binding.upcomingMoviesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                handleOnScrolled(recyclerView, dx, dy)
            }
        })
    }

    private fun navigateToDetail(movie: Movie) {
        val intent = Intent(requireContext(), MovieDetail::class.java)
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