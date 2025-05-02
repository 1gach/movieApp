package com.example.movieapp

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MovieViewModel: ViewModel() {

    private val searchQuery = MutableStateFlow("")

    fun setQuery(query: String) {
        searchQuery.value = query
    }

    val movies = searchQuery
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isBlank()) flow { emit(PagingData.empty()) }
            else Pager(PagingConfig(pageSize = 20)) {
                MoviePagingSource(RetrofitClient.api, "6df68ab00fa7dd19a922a50ec6885352", query)
            }.flow
        }
        .cachedIn(viewModelScope)

}




