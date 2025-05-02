package com.example.movieapp.model



data class MovieResponse(
    val page: Int,
    val results: List<Movies>,

    val total_pages: Int,
    val total_results: Int
)

data class Movies(
    val id: Int,
    val title: String,
    val poster_path: String?,
    val release_date: String
)




