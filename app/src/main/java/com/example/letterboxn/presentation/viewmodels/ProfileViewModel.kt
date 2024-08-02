package com.example.letterboxn.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxn.domain.model.RatedMovieItem
import com.example.letterboxn.domain.repository.RatedMovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val ratedMovieRepository : RatedMovieRepository
) : ViewModel() {

    val movies = MutableStateFlow<List<RatedMovieItem>>(emptyList())

    init {
        getMovies()
    }

    fun getMovies() {
        viewModelScope.launch {
            val response = ratedMovieRepository.getRatedMovies()
            movies.emit(response)
        }
    }
}