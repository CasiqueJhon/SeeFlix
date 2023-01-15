package com.example.movies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.movies.databinding.ActivityMainBinding
import com.example.movies.ui.adapter.ViewPagerAdapter
import com.example.movies.ui.mostRatedMovies.MostRatedMoviesFragment
import com.example.movies.ui.popularMovies.PopularMoviesFragment
import com.example.movies.ui.upcomingMovies.UpcomingMoviesFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpAdapter()
    }

    private fun setUpAdapter() {
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment(PopularMoviesFragment(), "Popular")
        viewPagerAdapter.addFragment(MostRatedMoviesFragment(), "Most Rated")
        viewPagerAdapter.addFragment(UpcomingMoviesFragment(), "Upcoming")

        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager

        viewPager.adapter = viewPagerAdapter
        viewPager.currentItem = 0
        tabLayout.setupWithViewPager(viewPager)

    }

}