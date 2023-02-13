package com.example.movies.db

import androidx.room.*

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favorite: Favorite)

    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): List<Favorite>

    @Delete
    fun delete(favorite: Favorite)
}