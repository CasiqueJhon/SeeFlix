package com.example.movies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import com.example.movies.databinding.ActivityMainBinding
import com.example.movies.ui.adapter.ViewPagerAdapter
import com.example.movies.ui.mostRatedMovies.MostRatedMoviesFragment
import com.example.movies.ui.popularMovies.PopularMoviesFragment
import com.example.movies.ui.searchMovie.SearchMovieFragment
import com.example.movies.ui.upcomingMovies.UpcomingMoviesFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpAdapter()
        prepareSearchView()
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

    private fun prepareSearchView() {
        searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                //this function will find the movie once the user submit
                Toast.makeText(this@MainActivity, "text submit", Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null && newText.isNotEmpty()) {
                    val searchMovieFragment = SearchMovieFragment.newInstance(newText)
                    val fragmentManager = supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.fragment_container, searchMovieFragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }
                return false
            }
        })
    }

}