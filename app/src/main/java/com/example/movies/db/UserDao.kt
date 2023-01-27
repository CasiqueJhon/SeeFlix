package com.example.movies.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.movies.result.Result

@Dao
interface UserDao {

    @Insert
    fun addUser(user: User)

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    fun getUsers(email: String, password: String) :User?

    @Delete
    fun deleteUser(user: User)
}