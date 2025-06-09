package com.example.movies.ui.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.auth.AuthManager
import com.example.movies.auth.AuthState
import com.example.movies.db.User
import com.example.movies.result.Result
import com.example.movies.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authManager: AuthManager,
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

    val authState: StateFlow<AuthState> = authManager.authState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AuthState.Initial
        )

    init {
        checkActiveSession()
    }

    private fun checkActiveSession() {
        viewModelScope.launch {
            authManager.getActiveSession()
                .collect { state ->
                    when (state) {
                        is AuthState.Authenticated -> {
                            // Validar y refrescar token si es necesario
                            authManager.validateAndRefreshTokenIfNeeded()
                        }
                        else -> {
                            // No hay sesión activa o hubo un error
                        }
                    }
                }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                when (val result = userRepository.getUser(email, password)) {
                    is Result.Success -> {
                        val user = result.data
                        if (user != null) {
                            // En un caso real, aquí obtendrías el token del backend
                            val mockToken = "mock_token_${System.currentTimeMillis()}"
                            val expiryTime = System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000) // 7 días
                            
                            authManager.saveSession(
                                userId = user.id ?: 0,
                                email = user.email,
                                token = mockToken,
                                expiryTimeMillis = expiryTime
                            )
                        } else {
                            authManager.clearSession()
                        }
                    }
                    is Result.Error -> {
                        authManager.clearSession()
                    }
                }
            } catch (e: Exception) {
                authManager.clearSession()
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authManager.clearSession()
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