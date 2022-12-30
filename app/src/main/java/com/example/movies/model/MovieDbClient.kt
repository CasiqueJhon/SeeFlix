package com.example.movies.model

import com.example.movies.constants.NetworkConstants
import com.example.movies.di.TheMovieDbService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MovieDbClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl(NetworkConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: TheMovieDbService = retrofit.create(TheMovieDbService::class.java)
}