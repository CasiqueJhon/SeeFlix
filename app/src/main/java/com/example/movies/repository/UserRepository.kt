package com.example.movies.repository

import android.content.Context
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

    companion object {
        const val SHARED_PREF_NAME = "user_shared_preferences"
    }

    suspend fun login(email: String, password: String): Result<User?> {
        return withContext(Dispatchers.IO) {
            try {
                val user = userDao.getUsers(email, password)
                if (user != null) {
                    Result.Success(user)
                } else {
                    Result.Error(Exception(ErrorConstants.invalidUser))
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
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

    fun saveUserToPreferences(user: User, context: Context) {
        val shadPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = shadPref.edit()
        editor.putString("email", user.email)
        editor.putString("password", user.password)
        editor.apply()
    }
}