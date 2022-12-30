package com.example.movies.repository

import com.example.movies.constants.NetworkConstants
import com.example.movies.model.MovieDbResult
import com.example.movies.di.TheMovieDbService
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val theMovieDbService: TheMovieDbService
) {

    suspend fun getPopularMovies(page: Int, pageSize: Int): MovieDbResult {
        return theMovieDbService.popularMoviesList(
            page,
            pageSize,
            NetworkConstants.ES_LANGUAGE,
            NetworkConstants.APY_KEY
        )
    }

    suspend fun getUpcomingMovies(page: Int, pageSize: Int): MovieDbResult {
        return theMovieDbService.upcomingMoviesList(
            page,
            pageSize,
            NetworkConstants.ES_LANGUAGE,
            NetworkConstants.APY_KEY
        )
    }

    suspend fun getMostRatedMovies(page: Int, pageSize: Int): MovieDbResult {
        return theMovieDbService.mostRatedMoviesList(
            page,
            pageSize,
            NetworkConstants.ES_LANGUAGE,
            NetworkConstants.APY_KEY
        )
    }
}