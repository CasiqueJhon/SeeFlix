package com.example.movies.ui.mostRatedMovies

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.constants.ErrorConstants
import com.example.movies.databinding.FragmentMostRatedMoviesBinding
import com.example.movies.model.Movie
import com.example.movies.repository.MovieRepository
import com.example.movies.ui.adapter.MoviesAdapter
import com.example.movies.ui.movieDetail.MovieDetail
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MostRatedMoviesFragment : Fragment() {

    @Inject
    lateinit var repository: MovieRepository
    private lateinit var gridLayoutManager: GridLayoutManager

    private var _binding: FragmentMostRatedMoviesBinding? = null
    private val binding: FragmentMostRatedMoviesBinding
        get() = _binding ?: throw Exception(ErrorConstants.generalError)

    private val mosRatedMoviesViewModel by viewModels<MosRatedMoviesViewModel>()
    private val moviesAdapter = MoviesAdapter { navigateToDetail (it) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMostRatedMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gridLayoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.mostRatedMoviesList.layoutManager = gridLayoutManager
        binding.mostRatedMoviesList.adapter = moviesAdapter

        mosRatedMoviesViewModel.fetchMostRatedMovies(1)
        mosRatedMoviesViewModel.movies.observe(viewLifecycleOwner) { movies ->
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
        val intent = Intent(requireContext(), MovieDetail::class.java)
        intent.putExtra(MovieDetail.EXTRA_MOVIE, movie)
        startActivity(intent)
    }

    private fun handleOnScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val layoutManager = recyclerView.layoutManager as GridLayoutManager
        val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
        val totalItemCount = layoutManager.itemCount
        if (totalItemCount - lastVisibleItem <= 5 && !mosRatedMoviesViewModel.isLoading) {
            mosRatedMoviesViewModel.currentPage++
            mosRatedMoviesViewModel.fetchMostRatedMovies(mosRatedMoviesViewModel.currentPage)
        }
    }


}