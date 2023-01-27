package com.example.movies.ui.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.db.User
import com.example.movies.result.Result
import com.example.movies.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _registerResult = MutableLiveData<Result<Boolean>>()
    val registerResult: LiveData<Result<Boolean>>
        get() = _registerResult
    private val _loginResult = MutableLiveData<Result<User?>>()
    val loginResult: LiveData<Result<User?>>
    get() = _loginResult
    private val _loggedInUSer = MutableLiveData<Result<User?>>()
    val loggedInUser: LiveData<Result<User?>>
    get() = _loggedInUSer
    private val _deleteResult = MutableLiveData<Result<User?>>()
    val deleteResult: LiveData<Result<User?>>
    get() = _deleteResult
    private val _getUserResult = MutableLiveData<Result<User?>>()
    val getUserResult: LiveData<Result<User?>>
    get() = _getUserResult

    fun doLogin(email: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = userRepository.login(email, password)
        }
    }

    fun insertUser(email: String, password: String) {
        val user = User(null, email, password)
        viewModelScope.launch {
            _registerResult.value = userRepository.insertUser(user)
        }
    }

    fun deleteUser(user: User, context: Context) {
        viewModelScope.launch {
            userRepository.removeUserFromPreferences(context)
            _deleteResult.value = userRepository.deleteUser(user)
        }
    }

    fun getUser(email: String, password: String) {
        viewModelScope.launch {
            _getUserResult.value = userRepository.getUser(email, password)
        }
    }

    fun saveUser(user: User, context: Context) {
        userRepository.saveUserToPreferences(user, context)
    }

    fun checkIfUserLoggedIn(email: String, password: String) {
        viewModelScope.launch {
            _loggedInUSer.value = userRepository.login(email, password)
        }
    }
}