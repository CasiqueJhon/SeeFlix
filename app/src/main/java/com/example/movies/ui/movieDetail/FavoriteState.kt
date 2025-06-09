package com.example.movies.ui.movieDetail

import com.example.movies.db.Favorite

sealed class FavoriteState {
    object Loading : FavoriteState()
    object Initial : FavoriteState()
    
    data class Success(
        val isFavorite: Boolean,
        val favorite: Favorite
    ) : FavoriteState()
    
    data class Error(
        val message: String,
        val exception: Throwable? = null
    ) : FavoriteState()
} 