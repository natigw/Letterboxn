package com.example.letterboxn.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxn.domain.model.MovieBackPosterItem
import com.example.letterboxn.domain.model.MovieItem
import com.example.letterboxn.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BShFavMoviesViewModel @Inject constructor(
    private val movieRepository : MovieRepository
) : ViewModel() {

    val posters = MutableStateFlow<List<MovieBackPosterItem>>(emptyList())

    init {
        getPosters(6)
    }

    fun getPosters(page: Int) {
        viewModelScope.launch {
            val response = movieRepository.getPosters(page)
            posters.emit(response)
        }
    }
}