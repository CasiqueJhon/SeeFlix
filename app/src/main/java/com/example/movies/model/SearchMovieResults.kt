package com.example.movies.model

import com.google.gson.annotations.SerializedName

data class SearchMovieResults(
    var page: Int,
    @SerializedName("results")
    var searchMovieResults: ArrayList<SearchMovie>,
    var totalPages: Int,
    var totalResults: Int
)
