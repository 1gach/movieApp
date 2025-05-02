package com.example.movieapp

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MoviePagingAdapter
    private val viewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MoviePagingAdapter()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Search input listener
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotBlank()) {
                        viewModel.setQuery(it)
                    } else {
                        // Submit empty list if query is blank
                        lifecycleScope.launch {
                            adapter.submitData(PagingData.empty())
                        }
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText.isNullOrEmpty()) {
                    // User clicked X or cleared the input → clear RecyclerView
                    lifecycleScope.launch {
                        adapter.submitData(PagingData.empty())
                    }
                }
                return true
            }
        })

        // Collect data and submit to adapter
        lifecycleScope.launch {
            viewModel.movies.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }

        // Handle empty result with Toast
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                val isListEmpty = loadStates.refresh is LoadState.NotLoading &&
                        adapter.itemCount == 0

                if (isListEmpty) {
                   binding.errorView.visibility = View.VISIBLE
                }else{
                    binding.errorView.visibility = View.INVISIBLE
                }
            }
        }
    }

}
