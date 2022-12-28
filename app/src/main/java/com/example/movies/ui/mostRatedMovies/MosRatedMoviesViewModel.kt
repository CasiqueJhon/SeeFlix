package com.example.movies.ui.mostRatedMovies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.model.Movie
import com.example.movies.repository.MovieRepository
import kotlinx.coroutines.launch

class MosRatedMoviesViewModel(private val movieRepository: MovieRepository): ViewModel() {

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

    fun fetchMostRatedMovies(page: Int) {
        viewModelScope.launch {
            val mostRatedMovies = movieRepository.getMostRatedMovies(page, pageSize)
            movies.value = mostRatedMovies.results
            isLoading = false
        }
    }
}