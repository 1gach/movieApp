package com.example.movieapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest

class MovieViewModel: ViewModel() {

    private val currentQuery = MutableStateFlow("")


    fun setQuery(query: String) {
        currentQuery.value = query
    }


    val movies = currentQuery
        .filter { it.isNotBlank() }
        .flatMapLatest { query ->
            Pager(PagingConfig(pageSize = 20)) {
                MoviePagingSource(RetrofitClient.api, "6df68ab00fa7dd19a922a50ec6885352", query)
            }.flow.cachedIn(viewModelScope)
        }

}





