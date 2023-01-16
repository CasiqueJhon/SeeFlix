package com.example.movies.network

import com.example.movies.model.CreditsResults
import com.example.movies.model.MovieDbResult
import com.example.movies.model.VideosResults
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDbService {

    @GET("movie/popular")
    suspend fun popularMoviesList(
        @Query("page") page: Int,
        @Query("total_pages") total_pages: Int,
        @Query("language") language: String,
        @Query("api_key") apiKey: String

    ): MovieDbResult

    @GET("movie/top_rated")
    suspend fun mostRatedMoviesList(
        @Query("page") page: Int,
        @Query("total_pages") total_pages: Int,
        @Query("language") language: String,
        @Query("api_key") apiKey: String
    ): MovieDbResult

    @GET("movie/upcoming")
    suspend fun upcomingMoviesList(
        @Query("page") page: Int,
        @Query("total_pages") total_pages: Int,
        @Query("language") language: String,
        @Query("api_key") apiKey: String
    ): MovieDbResult

    @GET("movie/{movie_id}/credits")
    suspend fun creditsList(
        @Path("movie_id") movie_id: Int?,
        @Query("api_key") apiKey: String
    ) : CreditsResults

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movie_id: Int?,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ) : VideosResults

    @GET("search/movie")
    suspend fun getMoviesBySearch(
        @Query("query") query: String,
        @Query("api_key") apiKey: String
    )

}