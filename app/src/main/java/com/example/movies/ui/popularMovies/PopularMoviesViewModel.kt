package com.example.movies.ui.popularMovies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.model.Movie
import com.example.movies.repository.MovieRepository
import kotlinx.coroutines.launch

class PopularMoviesViewModel(private val movieRepository: MovieRepository): ViewModel() {

    var currentPage = 1
    private val pageSize = 20
    var isLoading = false
    set(value) {
        field = value
        if (!value) {
            currentPage += 1
        }
    }

    val movies = MutableLiveData<List<Movie>>()

    fun fetchMovies(page: Int) {
        viewModelScope.launch {
            val popularMovies = movieRepository.getPopularMovies(page, pageSize)
            movies.value = popularMovies.results
            isLoading = false
        }
    }
}