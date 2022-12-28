package com.example.movies.model

data class CreditsResults(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int
)