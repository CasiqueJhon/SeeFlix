package com.example.movies.ui.popularMovies

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.constants.ErrorConstants
import com.example.movies.databinding.FragmentPopularMoviesBinding
import com.example.movies.model.Movie
import com.example.movies.repository.MovieRepository
import com.example.movies.ui.adapter.MoviesAdapter
import com.example.movies.ui.movieDetail.MovieDetailActivity
import com.example.movies.ui.user.UserActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PopularMoviesFragment : Fragment() {

    @Inject
    lateinit var repository: MovieRepository
    private lateinit var gridLayoutManager: GridLayoutManager

    private var _binding: FragmentPopularMoviesBinding? = null
    private val binding: FragmentPopularMoviesBinding
        get() = _binding ?: throw Exception(ErrorConstants.GENERAL_ERROR)

    private val popularMoviesViewModel: PopularMoviesViewModel by viewModels()
    private val moviesAdapter = MoviesAdapter { navigateToDetail (it) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPopularMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gridLayoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.moviesList.layoutManager = gridLayoutManager

        binding.moviesList.adapter = moviesAdapter

        popularMoviesViewModel.fetchPopularMovies(1)
        popularMoviesViewModel.movies.observe(viewLifecycleOwner) { movies ->
            if (!popularMoviesViewModel.isLoading) {
                moviesAdapter.appendMovies(movies)
                popularMoviesViewModel.isLoading = true
            }
        }
        binding.moviesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                handleOnScrolled(recyclerView, dx, dy)
            }
        })
        prepareBottomNavigationView()
    }

    private fun handleOnScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val layoutManager = recyclerView.layoutManager as GridLayoutManager
        val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
        val totalItemCount = layoutManager.itemCount
        if (totalItemCount - lastVisibleItem <= 5 && !popularMoviesViewModel.isLoading) {
            popularMoviesViewModel.currentPage++
            popularMoviesViewModel.fetchPopularMovies(popularMoviesViewModel.currentPage)
        }
    }

    private fun navigateToDetail(movie: Movie) {
        val intent = Intent(requireContext(), MovieDetailActivity::class.java)
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE, movie)
        startActivity(intent)
    }

    private fun prepareBottomNavigationView() {
        val bottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_search -> {

                }
                R.id.nav_favorites -> {

                }
                R.id.nav_profile -> {
                    val userIntent = Intent(requireContext(), UserActivity::class.java)
                    startActivity(userIntent)
                }
            }
            true
        }
    }
}




