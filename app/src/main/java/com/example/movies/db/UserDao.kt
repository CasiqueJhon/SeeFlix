package com.example.movies.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Insert
    fun addUser(user: User): Result<Boolean>

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    fun getUsers(email: String, password: String) :User?

    @Query("DELETE FROM users WHERE email = :email")
    fun deleteUser(email: String)
}