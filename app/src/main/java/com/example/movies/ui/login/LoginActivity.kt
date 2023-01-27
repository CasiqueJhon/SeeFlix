package com.example.movies.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.movies.MainActivity
import com.example.movies.constants.ErrorConstants
import com.example.movies.databinding.ActivityLoginBinding
import com.example.movies.result.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        login()
        launchSignup()
    }

    private fun login() {
        binding.loginButton.setOnClickListener {
            val email = binding.textInputEmail.text.toString()
            val password = binding.textInputPassword.text.toString()
            loginViewModel.doLogin(email, password)
            loginViewModel.loginResult.observe(this) { result ->
                when (result) {
                    is Result.Success -> {
                        val user = result.data
                        user?.let { userPref -> loginViewModel.saveUser(userPref, this) }
                        launchMainActivity()
                    }
                    is Result.Error -> {
                        Toast.makeText(this, ErrorConstants.INVALID_USER, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun launchSignup() {
        binding.txtSignUpOption.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun launchMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}