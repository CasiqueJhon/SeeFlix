package com.example.movies.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.movies.MainActivity
import com.example.movies.constants.ErrorConstants
import com.example.movies.databinding.ActivitySignupBinding
import com.example.movies.result.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        signup()
        launchLogin()
    }

    private fun signup() {
        val signUpButton = binding.signupBtn
        val email = binding.textInputEmailSignup
        val password = binding.textInputPasswordSignup
        val passwordRepeat = binding.textInputPasswordRepeat
        val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"
        signUpButton.setOnClickListener {
            val emailText = email.text.toString()
            val passwordText = password.text.toString()
            val passwordRepeatText = passwordRepeat.text.toString()
            if (emailText.isNotBlank()
                && passwordText.isNotBlank()
                && emailText.matches(emailRegex.toRegex())
                && passwordText.length >= 4
                && passwordRepeatText == passwordText
            ) {
                loginViewModel.insertUser(emailText, passwordText)
                loginViewModel.registerResult.observe(this) { result ->
                    when (result) {
                        is Result.Success -> {
                            Toast.makeText(
                                this,
                                "user registered successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        is Result.Error -> {
                            Toast.makeText(
                                this,
                                ErrorConstants.GENERAL_ERROR,
                                Toast.LENGTH_SHORT
                            )
                                .show()

                        }
                    }
                }
            } else {
                Toast.makeText(this, "Introduce a valid email", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun launchLogin() {
        binding.txtLoginOption.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}