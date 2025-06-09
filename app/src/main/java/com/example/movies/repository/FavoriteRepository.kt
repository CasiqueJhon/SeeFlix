package com.example.movies.repository

import androidx.lifecycle.LiveData
import com.example.movies.db.Favorite
import com.example.movies.db.FavoriteDao
import com.example.movies.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoriteRepository @Inject constructor(
    private val favoriteDao: FavoriteDao
) {
    suspend fun insertFavorite(favorite: Favorite): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                favoriteDao.insert(favorite)
                Result.Success(true)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    suspend fun getFavoriteMovie(id: Int): Result<Favorite?> {
        return withContext(Dispatchers.IO) {
            try {
                val favorite = favoriteDao.getFavoriteByMovieId(id)
                Result.Success(favorite)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    suspend fun updateMovie(favorite: Favorite): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                favoriteDao.updateMovie(favorite)
                Result.Success(true)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    suspend fun getAllFavorites(): Result<List<Favorite>> {
        return withContext(Dispatchers.IO) {
            try {
                val favorites = favoriteDao.getAllFavorites()
                Result.Success(favorites)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }
}