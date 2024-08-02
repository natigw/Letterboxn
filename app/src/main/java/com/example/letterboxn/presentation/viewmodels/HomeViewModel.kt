package com.example.letterboxn.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxn.domain.model.ListItem
import com.example.letterboxn.domain.model.MovieItem
import com.example.letterboxn.domain.repository.ListRepository
import com.example.letterboxn.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieRepository : MovieRepository,
    private val listRepository: ListRepository
//    private val personRepository : PersonRepo
) : ViewModel() {

    val movies = MutableStateFlow<List<MovieItem>>(emptyList())
    val lists = MutableStateFlow<List<ListItem>>(emptyList())

    init {
        getMovies(1)
        getLists()
    }

    fun getMovies(page: Int) {
        viewModelScope.launch {
            val response = movieRepository.getMovies(page)
            movies.emit(response)
        }
    }

    fun getLists() {
        viewModelScope.launch {
            val listresponse = listRepository.getLists()
            lists.emit(listresponse)
        }
    }

}