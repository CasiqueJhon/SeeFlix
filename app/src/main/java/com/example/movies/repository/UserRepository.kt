package com.example.movies.repository

import com.example.movies.db.User
import com.example.movies.db.UserDao
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao
) {

    fun login(email: String, password: String): Boolean {
        val user = userDao.getUsers(email, password)
        return user != null
    }

    fun insertUser(user: User): Result<Boolean> {
        return  userDao.addUser(user)
    }

    fun deleteUser(email: String) {
        userDao.deleteUser(email)
    }
}