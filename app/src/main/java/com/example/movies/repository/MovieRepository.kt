package com.example.movies.repository

import com.example.movies.constants.NetworkConstants
import com.example.movies.model.MovieDbClient
import com.example.movies.model.MovieDbResult

class MovieRepository {

    suspend fun getPopularMovies(page: Int, pageSize: Int): MovieDbResult {
        return MovieDbClient.service.popularMoviesList(
            page,
            pageSize,
            NetworkConstants.ES_LANGUAGE,
            NetworkConstants.APY_KEY
        )
    }

    suspend fun getUpcomingMovies(page: Int, pageSize: Int): MovieDbResult {
        return MovieDbClient.service.upcomingMoviesList(
            page,
            pageSize,
            NetworkConstants.ES_LANGUAGE,
            NetworkConstants.APY_KEY
        )
    }

    suspend fun getMostRatedMovies(page: Int, pageSize: Int): MovieDbResult {
        return MovieDbClient.service.mostRatedMoviesList(
            page,
            pageSize,
            NetworkConstants.ES_LANGUAGE,
            NetworkConstants.APY_KEY
        )
    }
}