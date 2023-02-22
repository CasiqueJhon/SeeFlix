package com.example.movies.ui.movieDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.constants.ErrorConstants
import com.example.movies.db.Favorite
import com.example.movies.model.Cast
import com.example.movies.model.Videos
import com.example.movies.repository.FavoriteRepository
import com.example.movies.repository.MovieRepository
import com.example.movies.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _movie = MutableLiveData<Favorite>()
    private val _characters = MutableLiveData<List<Cast>>()
    private val _videos = MutableLiveData<List<Videos>>()
    private val _movieFavoriteId = MutableLiveData<Result<Favorite?>>()
    private val _isFavorite = MutableLiveData<Boolean?>()

    val movie : LiveData<Favorite> = _movie
    val characters : LiveData<List<Cast>> = _characters
    val videos: LiveData<List<Videos>> = _videos
    val movieFavoriteId: LiveData<Result<Favorite?>> = _movieFavoriteId
    val isFavorite: LiveData<Boolean?> = _isFavorite

    fun fetchMovieCredits(movie_id: Int?) {
        viewModelScope.launch {
            val credits = movie_id?.let { movieRepository.getMovieCredits(it) }
            _characters.value = credits?.cast
        }
    }

    fun fetchMovieVideos(movie_id: Int?) {
        viewModelScope.launch {
            val movieVideos = movie_id?.let { movieRepository.getMovieVideos(it) }
            _videos.value = movieVideos?.videos
        }
    }

    fun addToFavorites(favorite: Favorite) {
        favorite.isFavorite = true
        viewModelScope.launch {
            favoriteRepository.insertFavorite(favorite)
            favoriteRepository.updateMovie(favorite)
        }
    }

    fun getFavoriteMovie(id: Int) {
        viewModelScope.launch {
            _movieFavoriteId.value = favoriteRepository.getFavoriteMovie(id)
        }
    }

    fun isFavoriteMovie(id: Int) {
        viewModelScope.launch {
            favoriteRepository.getFavoriteMovie(id).let { result ->
                if (result is Result.Success) {
                    _isFavorite.value = true
                } else if (result is Result.Error) {
                    _isFavorite.value = false
                }
            }
        }
    }

}