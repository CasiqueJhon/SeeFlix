package com.example.movies.ui.user

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.movies.MainActivity
import com.example.movies.R
import com.example.movies.constants.ErrorConstants
import com.example.movies.databinding.ActivityUserBinding
import com.example.movies.repository.UserRepository
import com.example.movies.repository.UserRepository.Companion.SHARED_PREF_NAME
import com.example.movies.result.Result
import com.example.movies.ui.login.LoginActivity
import com.example.movies.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UserActivity : AppCompatActivity() {

    @Inject
    lateinit var repository: UserRepository
    private lateinit var binding: ActivityUserBinding

    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        logout()
        prepareBottomNavigationView()
    }

    private fun logout() {
        binding.txtLogout.setOnClickListener {
            val sharedPref = this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            val email = sharedPref.getString("email", "")
            val password = sharedPref.getString("password", "")
            email?.let { emailData -> password?.let { passwordData -> loginViewModel.getUser(emailData, passwordData) } }
            loginViewModel.getUserResult.observe(this) { user ->
                when (user) {
                    is Result.Success -> {
                        user.data?.let { userData -> loginViewModel.deleteUser(userData, this) }
                        loginViewModel.deleteResult.observe(this) { userDelete ->
                            when(userDelete) {
                                is Result.Success -> {
                                    userDelete.data?.let { userDeleteData -> loginViewModel.deleteUser(userDeleteData, this) }
                                    launchLoginActivity()
                                }
                                is Result.Error -> {
                                    Toast.makeText(this, ErrorConstants.GENERAL_ERROR, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                    is Result.Error -> {
                        Toast.makeText(this, ErrorConstants.GENERAL_ERROR, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun launchLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun prepareBottomNavigationView() {
        val bottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_search -> {

                }
                R.id.nav_favorites -> {

                }
            }
            true
        }
    }
}