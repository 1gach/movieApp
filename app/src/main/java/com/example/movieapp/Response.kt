package com.example.movieapp



data class Response(
    val results: List<Movies>,
    val page: Int,
    val total_pages: Int)

data class Movies(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String
)

