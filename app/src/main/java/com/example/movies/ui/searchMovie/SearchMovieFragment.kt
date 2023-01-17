package com.example.movies.ui.searchMovie

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movies.MainActivity
import com.example.movies.R
import com.example.movies.constants.ErrorConstants
import com.example.movies.databinding.FragmentSearchMovieBinding
import com.example.movies.model.Movie
import com.example.movies.repository.MovieRepository
import com.example.movies.ui.adapter.MoviesAdapter
import com.example.movies.ui.movieDetail.MovieDetail
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchMovieFragment : Fragment() {

    @Inject
    lateinit var repository: MovieRepository
    private lateinit var gridLayout: GridLayoutManager

    private var _binding: FragmentSearchMovieBinding? = null
    private val binding: FragmentSearchMovieBinding
        get() = _binding ?: throw Exception(ErrorConstants.fragmentError)
    private var query: String = ""

    private val searchFragmentViewModel by viewModels<SearchMovieViewModel>()
    private val moviesAdapter = MoviesAdapter { navigateToDetail (it) }

    companion object {
        private const val NEW_TEXT_SEARCH = "newText"

        fun newInstance(newText: String): SearchMovieFragment {
            val args = Bundle().apply {
                putString(NEW_TEXT_SEARCH, newText)
            }
            return SearchMovieFragment().apply {
                arguments = args
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gridLayout =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.searchMoviesList.layoutManager = gridLayout
        binding.searchMoviesList.adapter = moviesAdapter
        query = arguments?.getString(NEW_TEXT_SEARCH) ?: return
        searchFragmentViewModel.fetchMoviesBySearch(query)
        searchFragmentViewModel.moviesSearch.observe(viewLifecycleOwner) { movies ->
            if (!searchFragmentViewModel.isLoading) {
                moviesAdapter.appendMovies(movies)
                searchFragmentViewModel.isLoading = true
            }
        }
        prepareBottomNavigationView()
    }

    private fun navigateToDetail(movie: Movie) {
        val intent = Intent(requireContext(), MovieDetail::class.java)
        intent.putExtra(MovieDetail.EXTRA_MOVIE, movie)
        startActivity(intent)
    }

    private fun prepareBottomNavigationView() {
        val bottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_search -> {

                }
                R.id.nav_favorites -> {

                }
                R.id.nav_profile -> {

                }
            }
            true
        }
    }
}