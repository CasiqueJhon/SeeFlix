package com.example.movies.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

private val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

@Singleton
class AuthManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferencesKeys {
        val USER_ID = intPreferencesKey("user_id")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val AUTH_TOKEN = stringPreferencesKey("auth_token")
        val TOKEN_EXPIRY = longPreferencesKey("token_expiry")
    }

    private val authDataStore = context.authDataStore

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    suspend fun saveSession(userId: Int, email: String, token: String, expiryTimeMillis: Long) {
        try {
            authDataStore.edit { preferences ->
                preferences[PreferencesKeys.USER_ID] = userId
                preferences[PreferencesKeys.USER_EMAIL] = email
                preferences[PreferencesKeys.AUTH_TOKEN] = token
                preferences[PreferencesKeys.TOKEN_EXPIRY] = expiryTimeMillis
            }
            _authState.value = AuthState.Authenticated(userId, email, token)
        } catch (e: Exception) {
            _authState.value = AuthState.Error("Error al guardar la sesión", e)
        }
    }

    suspend fun clearSession() {
        try {
            authDataStore.edit { preferences ->
                preferences.clear()
            }
            _authState.value = AuthState.Unauthorized
        } catch (e: Exception) {
            _authState.value = AuthState.Error("Error al cerrar sesión", e)
        }
    }

    fun getActiveSession(): Flow<AuthState> = flow {
        emit(AuthState.Loading)
        try {
            authDataStore.data.first().let { preferences ->
                val userId = preferences[PreferencesKeys.USER_ID]
                val email = preferences[PreferencesKeys.USER_EMAIL]
                val token = preferences[PreferencesKeys.AUTH_TOKEN]
                val expiry = preferences[PreferencesKeys.TOKEN_EXPIRY]

                if (userId != null && email != null && token != null && expiry != null) {
                    if (System.currentTimeMillis() < expiry) {
                        emit(AuthState.Authenticated(userId, email, token))
                    } else {
                        clearSession()
                        emit(AuthState.Unauthorized)
                    }
                } else {
                    emit(AuthState.Unauthorized)
                }
            }
        } catch (e: Exception) {
            emit(AuthState.Error("Error al recuperar la sesión", e))
        }
    }

    suspend fun validateAndRefreshTokenIfNeeded() {
        try {
            authDataStore.data.first().let { preferences ->
                val expiry = preferences[PreferencesKeys.TOKEN_EXPIRY] ?: 0L
                if (System.currentTimeMillis() > expiry - TOKEN_REFRESH_THRESHOLD) {
                    // Aquí iría la lógica para refrescar el token con el backend
                    // Por ahora solo manejamos la expiración
                    if (System.currentTimeMillis() > expiry) {
                        clearSession()
                    }
                }
            }
        } catch (e: Exception) {
            _authState.value = AuthState.Error("Error al validar el token", e)
        }
    }

    companion object {
        private const val TOKEN_REFRESH_THRESHOLD = 24 * 60 * 60 * 1000L // 24 horas en milisegundos
    }
} 