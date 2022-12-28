package com.example.movies.model

data class MovieDbResult(
    val page: Int,
    val results: MutableList<Movie>,
    val total_pages: Int,
    val total_results: Int
)