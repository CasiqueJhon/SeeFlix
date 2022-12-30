package com.example.movies.di

import com.example.movies.model.CreditsResults
import com.example.movies.model.MovieDbResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

@JvmSuppressWildcards
interface TheMovieDbService {

    @GET("movie/popular")
    @JvmSuppressWildcards
    suspend fun popularMoviesList(
        @Query("page") page: Int,
        @Query("total_pages") total_pages: Int,
        @Query("language") language: String,
        @Query("api_key") apiKey: String

    ): MovieDbResult

    @GET("movie/top_rated")
    @JvmSuppressWildcards
    suspend fun mostRatedMoviesList(
        @Query("page") page: Int,
        @Query("total_pages") total_pages: Int,
        @Query("language") language: String,
        @Query("api_key") apiKey: String
    ): MovieDbResult

    @GET("movie/upcoming")
    @JvmSuppressWildcards
    suspend fun upcomingMoviesList(
        @Query("page") page: Int,
        @Query("total_pages") total_pages: Int,
        @Query("language") language: String,
        @Query("api_key") apiKey: String
    ): MovieDbResult

    @GET("movie/{movie_id}/credits")
    @JvmSuppressWildcards
    suspend fun creditsList(
        @Path("movie_id") movie_id: Int?,
        @Query("api_key") apiKey: String
    ) : CreditsResults

}