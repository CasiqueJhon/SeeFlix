package com.example.movies.model

import com.google.gson.annotations.SerializedName

data class SearchMovie(
    var adult: Boolean,
    @SerializedName("backdrop_path")
    var backdropPath: String,
    var genreIds: ArrayList<Int>,
    var id: Int,
    var originalLanguage : String,
    var originalTitle: String,
    @SerializedName("overview")
    var overview: String,
    var popularity: Double,
    @SerializedName("poster_path")
    var posterPath: String,
    var releaseDate: String,
    var title: String,
    var video: Boolean,
    var voteAverage: Double,
    var voteCount: Int
)
