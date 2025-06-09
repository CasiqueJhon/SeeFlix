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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _favoriteState = MutableStateFlow<FavoriteState>(FavoriteState.Initial)
    val favoriteState: StateFlow<FavoriteState> = _favoriteState.asStateFlow()

    private val _characters = MutableLiveData<List<Cast>>()
    private val _videos = MutableLiveData<List<Videos>>()

    val characters: LiveData<List<Cast>> = _characters
    val videos: LiveData<List<Videos>> = _videos

    fun fetchMovieCredits(movie_id: Int?) {
        viewModelScope.launch {
            try {
                val credits = movie_id?.let { movieRepository.getMovieCredits(it) }
                _characters.value = credits?.cast
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }

    fun fetchMovieVideos(movie_id: Int?) {
        viewModelScope.launch {
            try {
                val movieVideos = movie_id?.let { movieRepository.getMovieVideos(it) }
                _videos.value = movieVideos?.videos
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }

    fun toggleFavorite(movieId: Int, favorite: Favorite) {
        viewModelScope.launch {
            try {
                _favoriteState.value = FavoriteState.Loading
                
                val currentState = when (val result = favoriteRepository.getFavoriteMovie(movieId)) {
                    is Result.Success -> {
                        val currentFavorite = result.data
                        if (currentFavorite != null) {
                            currentFavorite.isFavorite = !currentFavorite.isFavorite
                            favoriteRepository.updateMovie(currentFavorite)
                            FavoriteState.Success(currentFavorite.isFavorite, currentFavorite)
                        } else {
                            favorite.isFavorite = true
                            favoriteRepository.insertFavorite(favorite)
                            FavoriteState.Success(true, favorite)
                        }
                    }
                    is Result.Error -> {
                        favorite.isFavorite = true
                        favoriteRepository.insertFavorite(favorite)
                        FavoriteState.Success(true, favorite)
                    }
                }
                
                _favoriteState.value = currentState
            } catch (e: Exception) {
                _favoriteState.value = FavoriteState.Error(
                    message = "Error al actualizar favorito",
                    exception = e
                )
            }
        }
    }

    fun checkFavoriteStatus(movieId: Int) {
        viewModelScope.launch {
            try {
                _favoriteState.value = FavoriteState.Loading
                
                when (val result = favoriteRepository.getFavoriteMovie(movieId)) {
                    is Result.Success -> {
                        val favorite = result.data
                        if (favorite != null) {
                            _favoriteState.value = FavoriteState.Success(
                                isFavorite = favorite.isFavorite,
                                favorite = favorite
                            )
                        } else {
                            _favoriteState.value = FavoriteState.Success(
                                isFavorite = false,
                                favorite = Favorite(movieId = movieId)
                            )
                        }
                    }
                    is Result.Error -> {
                        _favoriteState.value = FavoriteState.Error(
                            message = "Error al verificar estado de favorito",
                            exception = result.exception
                        )
                    }
                }
            } catch (e: Exception) {
                _favoriteState.value = FavoriteState.Error(
                    message = "Error al verificar estado de favorito",
                    exception = e
                )
            }
        }
    }
}