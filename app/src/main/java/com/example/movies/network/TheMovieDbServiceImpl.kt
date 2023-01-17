package com.example.movies.network

import com.example.movies.constants.NetworkConstants
import com.example.movies.model.CreditsResults
import com.example.movies.model.MovieDbResult
import com.example.movies.model.VideosResults
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class TheMovieDbServiceImpl @Inject constructor(): TheMovieDbService {

    private val retrofit =  Retrofit.Builder()
        .baseUrl(NetworkConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service: TheMovieDbService = retrofit.create(TheMovieDbService::class.java)

    override suspend fun popularMoviesList(
        page: Int,
        total_pages: Int,
        language: String,
        apiKey: String
    ): MovieDbResult {
        return service.popularMoviesList(page, total_pages, language, apiKey)
    }

    override suspend fun mostRatedMoviesList(
        page: Int,
        total_pages: Int,
        language: String,
        apiKey: String
    ): MovieDbResult {
        return service.mostRatedMoviesList(page, total_pages, language, apiKey)
    }

    override suspend fun upcomingMoviesList(
        page: Int,
        total_pages: Int,
        language: String,
        apiKey: String
    ): MovieDbResult {
        return service.upcomingMoviesList(page, total_pages, language, apiKey)
    }

    override suspend fun creditsList(movie_id: Int?, apiKey: String): CreditsResults {
        return service.creditsList(movie_id, apiKey)
    }

    override suspend fun getMovieVideos(movie_id: Int?, apiKey: String, language: String): VideosResults {
        return service.getMovieVideos(movie_id, apiKey, language)
    }

    override suspend fun getMoviesBySearch(query: String, apiKey: String) : MovieDbResult {
        return service.getMoviesBySearch(query, apiKey)
    }
}