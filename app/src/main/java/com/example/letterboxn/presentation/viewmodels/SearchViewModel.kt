package com.example.letterboxn.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxn.domain.model.SearchItem
import com.example.letterboxn.domain.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    val results = MutableStateFlow<List<SearchItem>>(emptyList())

    init {
        searchMovies(movieName = "Monkey man")
    }

    private fun searchMovies(movieName : String) {
        viewModelScope.launch {
            val response = searchRepository.searchMovies(movieName)
            results.emit(response)
        }
    }
}