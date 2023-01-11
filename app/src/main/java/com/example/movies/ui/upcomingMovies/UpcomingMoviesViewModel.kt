package com.example.movies.ui.upcomingMovies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.model.Movie
import com.example.movies.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpcomingMoviesViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

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

    fun fetchUpcomingMovies(page: Int) {
        viewModelScope.launch {
            val upcomingMovies = movieRepository.getUpcomingMovies(page, pageSize)
            movies.value = upcomingMovies.results
            isLoading = false
        }
    }
}