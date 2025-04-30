package com.example.movieapp


import androidx.paging.PagingSource
import androidx.paging.PagingState


class MoviePagingSource(
        private val api: MovieApi,
        private val apiKey: String,
        private val query: String // Add query parameter
    ) : PagingSource<Int, Movies>() {

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movies> {
            return try {
                val currentPage = params.key ?: 1
                val response = api.getPopularMovies(apiKey, query, currentPage)
                LoadResult.Page(
                    data = response.results,
                    prevKey = if (currentPage == 1) null else currentPage - 1,
                    nextKey = if (currentPage < response.total_pages) currentPage + 1 else null
                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, Movies>): Int? {
            return state.anchorPosition
        }
    }

