package com.example.movies.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.movies.MainActivity
import com.example.movies.R
import com.example.movies.auth.AuthState
import com.example.movies.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAuthStateObserver()
        setupLoginButton()
    }

    private fun setupAuthStateObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.authState.collect { state ->
                    when (state) {
                        is AuthState.Initial -> {
                            // Estado inicial, no hacer nada
                        }
                        is AuthState.Loading -> {
                            // Mostrar progreso
                            binding.loginButton.isEnabled = false
                        }
                        is AuthState.Authenticated -> {
                            // Usuario autenticado, navegar a MainActivity
                            binding.loginButton.isEnabled = true
                            navigateToMain()
                        }
                        is AuthState.Unauthorized -> {
                            // Usuario no autenticado, mostrar campos de login
                            binding.loginButton.isEnabled = true
                        }
                        is AuthState.Error -> {
                            // Mostrar error
                            binding.loginButton.isEnabled = true
                            Toast.makeText(
                                this@LoginActivity,
                                state.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun setupLoginButton() {
        binding.loginButton.setOnClickListener {
            val email = binding.textInputEmail.text.toString()
            val password = binding.textInputPassword.text.toString()

            if (validateInputs(email, password)) {
                loginViewModel.login(email, password)
            }
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        var isValid = true

        if (email.isEmpty()) {
            binding.textInputEmail.error = getString(R.string.error_empty_email)
            isValid = false
        }

        if (password.isEmpty()) {
            binding.textInputPassword.error = getString(R.string.error_empty_password)
            isValid = false
        }

        return isValid
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}