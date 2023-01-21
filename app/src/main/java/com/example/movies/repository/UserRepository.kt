package com.example.movies.repository

import com.example.movies.constants.ErrorConstants
import com.example.movies.db.User
import com.example.movies.db.UserDao
import com.example.movies.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao
) {

    fun login(email: String, password: String): Boolean {
        val user = userDao.getUsers(email, password)
        return user != null
    }

    suspend fun insertUser(user: User): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                userDao.addUser(user)
                Result.Success(true)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    fun deleteUser(email: String) {
        userDao.deleteUser(email)
    }
}