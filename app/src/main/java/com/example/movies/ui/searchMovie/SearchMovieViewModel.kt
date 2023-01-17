package com.example.movies.ui.searchMovie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.model.Movie
import com.example.movies.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchMovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    var isLoading = false
    val moviesSearch = MutableLiveData<List<Movie>>()

    fun fetchMoviesBySearch(query: String) {
        viewModelScope.launch {
            val movieSearch = movieRepository.getMovieSearch(query)
            moviesSearch.value = movieSearch.results
        }
    }
}