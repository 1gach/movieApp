package com.example.movieapp

import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String, // Query parameter for search
        @Query("page") page: Int
    ): Response
}