package com.example.movies.ui.splash

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.viewModels
import com.example.movies.MainActivity
import com.example.movies.databinding.ActivitySplashScreenBinding
import com.example.movies.result.Result
import com.example.movies.ui.login.LoginActivity
import com.example.movies.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

    companion object {
        const val SHARED_PREF_NAME = "user_shared_preferences"
    }

    private lateinit var binding: ActivitySplashScreenBinding

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        isUserLogged()
    }

    private fun isUserLogged() {
        Handler().postDelayed({
            val sharedPref = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            val email = sharedPref.getString("email", null)
            val password = sharedPref.getString("password", null)
            if (email != null && password != null) {
                loginViewModel.checkIfUserLoggedIn(email, password)
                loginViewModel.loggedInUser.observe(this) { user ->
                    when (user) {
                        is Result.Success -> {
                            launchMainActivity()
                        }
                        is Result.Error -> {
                            Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show()
                            launchLoginActivity()
                        }
                    }
                }
            } else {
                launchLoginActivity()
            }
        }, 3000)
    }

    private fun launchMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun launchLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}