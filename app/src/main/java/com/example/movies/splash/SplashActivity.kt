package com.example.movies.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.movies.MainActivity
import com.example.movies.R
import com.example.movies.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTheme(R.style.SplashTheme)
        startActivity(Intent(this, MainActivity::class.java))
        Thread.sleep(2000)
        finish()
    }
}