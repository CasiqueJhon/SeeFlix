package com.example.movies.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class, Favorite::class], version = 3)
abstract class AppDatabase: RoomDatabase() {

    abstract val userDao: UserDao
    abstract val favoriteDao: FavoriteDao
}