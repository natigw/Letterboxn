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

    val searchResults = MutableStateFlow<List<SearchItem>>(emptyList())
    val multiSearchResults = MutableStateFlow<List<SearchItem>>(emptyList())

    fun searchMovies(movieToSearch : String) {
        viewModelScope.launch {
            val responseSearch = searchRepository.searchMovies(movieToSearch)
            searchResults.emit(responseSearch)
        }
    }

    fun searchMoviesMulti(movieToSearch: String) {
        viewModelScope.launch {
            val responseMultiSearch = searchRepository.searchMoviesMulti(movieToSearch)
            multiSearchResults.emit(responseMultiSearch)
        }
    }
}