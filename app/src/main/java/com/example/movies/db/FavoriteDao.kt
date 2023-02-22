package com.example.movies.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.movies.model.Movie
import com.example.movies.result.Result

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favorite: Favorite)

    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): List<Favorite>

    @Query("SELECT * FROM favorites WHERE movie_id = :movieId")
    fun getFavoriteByMovieId(movieId: Int): Favorite?

    @Delete
    fun delete(favorite: Favorite)

    @Update
    fun updateMovie(favorite: Favorite)
}