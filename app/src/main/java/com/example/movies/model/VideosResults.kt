package com.example.movies.model

import com.google.gson.annotations.SerializedName

data class VideosResults(
    @SerializedName("results")
    val videos: List<Videos>,
    val id: Int
)
