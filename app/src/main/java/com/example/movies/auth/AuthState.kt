package com.example.movies.auth

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    object Unauthorized : AuthState()
    
    data class Authenticated(
        val userId: Int,
        val email: String,
        val token: String
    ) : AuthState()
    
    data class Error(
        val message: String,
        val exception: Throwable? = null
    ) : AuthState()
} 