package com.example.movies.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.db.User
import com.example.movies.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private var userId: Int? = 0
    private val _registerResult = MutableLiveData<Result<Boolean>>()
    val registerResult: LiveData<Result<Boolean>>
        get() = _registerResult

    fun login(email: String, password: String): Boolean {
        return userRepository.login(email, password)
    }

    fun insertUser(email: String, password: String) {
        val user = User(userId, email, password)
        viewModelScope.launch {
            _registerResult.value = userRepository.insertUser(user)
        }
    }

    fun deleteUser(email: String) {
        userRepository.deleteUser(email)
    }
}