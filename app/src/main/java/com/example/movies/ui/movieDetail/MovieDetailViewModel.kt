package com.example.movies.ui.movieDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.model.Cast
import com.example.movies.model.Movie
import com.example.movies.repository.MovieRepository
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _movie = MutableLiveData<Movie>()
    private val _characters = MutableLiveData<List<Cast>>()

    var movieId: Int = 0
    val movie : LiveData<Movie> = _movie
    val characters : LiveData<List<Cast>> = _characters

    fun fetchMovieCredits(movie_id: Int?) {
        viewModelScope.launch {
            val credits = movieRepository.getMovieCredits(movieId)
            _characters.value = credits.cast
        }
    }

}