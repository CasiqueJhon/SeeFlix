package com.example.movies.repository

import com.example.movies.constants.NetworkConstants
import com.example.movies.model.MovieDbResult
import com.example.movies.network.TheMovieDbServiceImpl
import com.example.movies.model.CreditsResults
import com.example.movies.model.VideosResults
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val theMovieDbService: TheMovieDbServiceImpl
) {

    suspend fun getPopularMovies(page: Int, pageSize: Int): MovieDbResult {
        return theMovieDbService.popularMoviesList(
            page,
            pageSize,
            NetworkConstants.ES_LANGUAGE,
            NetworkConstants.API_KEY
        )
    }

    suspend fun getUpcomingMovies(page: Int, pageSize: Int): MovieDbResult {
        return theMovieDbService.upcomingMoviesList(
            page,
            pageSize,
            NetworkConstants.ES_LANGUAGE,
            NetworkConstants.API_KEY
        )
    }

    suspend fun getMostRatedMovies(page: Int, pageSize: Int): MovieDbResult {
        return theMovieDbService.mostRatedMoviesList(
            page,
            pageSize,
            NetworkConstants.ES_LANGUAGE,
            NetworkConstants.API_KEY
        )
    }

    suspend fun getMovieCredits(movieId: Int): CreditsResults {
        return theMovieDbService.creditsList(movieId, NetworkConstants.API_KEY)
    }

    suspend fun getMovieVideos(movieId: Int) : VideosResults {
        return theMovieDbService.getMovieVideos(movieId, NetworkConstants.API_KEY, NetworkConstants.ES_LANGUAGE)
    }

    suspend fun getMovieSearch(query: String) : MovieDbResult {
        return theMovieDbService.getMoviesBySearch(query, NetworkConstants.API_KEY)
    }

}