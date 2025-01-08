package com.example.letterboxn.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxn.domain.model.MovieItem
import com.example.letterboxn.domain.model.home.popularLists.PopularListItem
import com.example.letterboxn.domain.model.home.recentReviews.ReviewWithMovieItem
import com.example.letterboxn.domain.repository.MovieRepository
import com.example.letterboxn.domain.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieRepository : MovieRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    val popularMovies = MutableStateFlow<List<MovieItem>>(emptyList())
    val popularLists = MutableStateFlow<List<PopularListItem>>(emptyList())
    val recentReviews = MutableStateFlow<List<ReviewWithMovieItem>>(emptyList())

    init {
        getMovies(1)
        getLists()
        getReviews()
    }

    private fun getMovies(page: Int) {
        viewModelScope.launch {
            val movies = movieRepository.getMovies(page)
            popularMovies.emit(movies)
        }
    }

    private fun getLists() {
        viewModelScope.launch {
            val collections = movieRepository.getLists()
            popularLists.update { collections }
        }
    }

    private fun getReviews() {
        viewModelScope.launch {
            val reviews = reviewRepository.getReviewsWithMovies()
            recentReviews.update { reviews }
        }
    }
}