package com.example.movies.repository

import com.example.movies.constants.NetworkConstants
import com.example.movies.model.MovieDbResult
import com.example.movies.di.TheMovieDbService
import com.example.movies.di.TheMovieDbServiceImpl
import com.example.movies.model.CreditsResults
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val theMovieDbService: TheMovieDbServiceImpl
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

    suspend fun getMovieCredits(movieId: Int): CreditsResults {
        return theMovieDbService.creditsList(movieId, NetworkConstants.APY_KEY)
    }

}