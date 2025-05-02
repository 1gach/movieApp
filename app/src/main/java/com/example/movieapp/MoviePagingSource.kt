package com.example.movieapp


import android.graphics.Movie
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movieapp.model.Movies


class MoviePagingSource(
    private val api: MovieApi,
    private val apiKey: String,
    private val query: String
) : PagingSource<Int, Movies>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movies> {
        return try {
            val page = params.key ?: 1
            val response = api.searchMovies(apiKey, query, page)
            LoadResult.Page(
                data = response.results,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page < response.total_pages) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movies>): Int? {
        return state.anchorPosition
    }
    }

