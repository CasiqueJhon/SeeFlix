package com.example.movies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.movies.databinding.ActivityMainBinding
import com.example.movies.ui.mostRatedMovies.MostRatedMovies
import com.example.movies.ui.popularMovies.PopularMoviesActivity
import com.example.movies.ui.upcomingMovies.UpcomingMovies
import dagger.hilt.android.HiltAndroidApp

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.txtMostPopularMovies.setOnClickListener { showMostPopularMovies() }
        binding.txtMostRatedMovies.setOnClickListener { showMostRatedMovies() }
        binding.txtUpcomingMovies.setOnClickListener { showUpcomingMovies() }
    }

    private fun showUpcomingMovies() {
        startActivity(Intent(this, UpcomingMovies::class.java))
    }

    private fun showMostRatedMovies() {
        startActivity(Intent(this, MostRatedMovies::class.java))
    }

    private fun showMostPopularMovies() {
        startActivity(Intent(this, PopularMoviesActivity::class.java))
    }

}