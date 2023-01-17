package com.example.movies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentManager
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
    private lateinit var searchMovieFragment: SearchMovieFragment

    private val fragmentManager = supportFragmentManager
    private val fragmentTransaction = fragmentManager.beginTransaction()
    private var fragmentCommitted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpAdapter()
    }

    private fun init() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, PopularMoviesFragment())
            .commit()
    }

    private fun setUpAdapter() {
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment(PopularMoviesFragment(), "Popular")
        viewPagerAdapter.addFragment(MostRatedMoviesFragment(), "Most Rated")
        viewPagerAdapter.addFragment(UpcomingMoviesFragment(), "Upcoming")
        prepareSearchView()

        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager

        viewPager.adapter = viewPagerAdapter
        viewPager.currentItem = 0
        tabLayout.setupWithViewPager(viewPager)

    }

    private fun prepareSearchView() {
        searchView = binding.searchView
        searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                launchSearchFragment(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //launchSearchFragment(newText)
                return false
            }

            private fun launchSearchFragment(newText: String?): Boolean {
                if (newText != null && newText.isNotEmpty()) {
                    searchMovieFragment = SearchMovieFragment.newInstance(newText)
                    fragmentTransaction.replace(R.id.fragment_container, searchMovieFragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentManager.popBackStack("SearchMovieFragment", 0)
                    fragmentTransaction.show(searchMovieFragment)
                    if (!fragmentCommitted)
                        fragmentTransaction.commit()
                    fragmentCommitted = true
                }
                return false
            }
        })
        searchView.setOnCloseListener {
            fragmentTransaction.hide(searchMovieFragment)
            searchView.setQuery("", false)
            searchView.clearFocus()
            fragmentTransaction.commit()
            fragmentCommitted = false
            false
        }
    }

}