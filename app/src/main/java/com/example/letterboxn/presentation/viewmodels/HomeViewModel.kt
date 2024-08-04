package com.example.letterboxn.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxn.domain.model.ListItem
import com.example.letterboxn.domain.model.ListWithAuthor
import com.example.letterboxn.domain.model.MovieItem
import com.example.letterboxn.domain.repository.ListRepository
import com.example.letterboxn.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieRepository : MovieRepository,
    private val listRepository: ListRepository
) : ViewModel() {

    val movies = MutableStateFlow<List<MovieItem>>(emptyList())
    val lists = MutableStateFlow<ListWithAuthor?>(null)

    init {
        getMovies(1)
        getLists()
    }

    private fun getMovies(page: Int) {
        viewModelScope.launch {
            val response = movieRepository.getMovies(page)
            movies.emit(response)
        }
    }

    private fun getLists() {
        viewModelScope.launch {
            val listResponse = listRepository.getLists()
            val authorResponse = listRepository.getUser()
            val combinedResponse = ListWithAuthor(listResponse, authorResponse)
            lists.update { combinedResponse }
        }
    }

}