package com.example.movies.ui.popularMovies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.model.Movie
import com.example.movies.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularMoviesViewModel @Inject constructor (private val movieRepository: MovieRepository): ViewModel() {

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

    fun fetchPopularMovies(page: Int) {
        viewModelScope.launch {
            val popularMovies = movieRepository.getPopularMovies(page, pageSize)
            movies.value = popularMovies.results
            isLoading = false
        }
    }
}