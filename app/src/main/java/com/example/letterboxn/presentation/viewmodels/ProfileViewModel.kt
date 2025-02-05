package com.example.letterboxn.presentation.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.example.letterboxn.data.local.database.review.ReviewDao
import com.example.letterboxn.data.remote.api.MovieApi
import com.example.letterboxn.domain.model.MovieDetailsForReviewItem
import com.example.letterboxn.domain.model.MovieItem
import com.example.letterboxn.domain.model.RatedMovieItem
import com.example.letterboxn.domain.model.ReviewLocalItem
import com.example.letterboxn.domain.repository.MovieRepository
import com.example.letterboxn.domain.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @Named("UserLoggedIn")
    val sharedPrefLogon: SharedPreferences,
    @Named("UserProfileImage")
    val sharedPrefProfilePicture: SharedPreferences,
    @Named("UserBackPosterImage")
    val sharedPrefBackPoster: SharedPreferences,
    @Named("UserBackPosterIsDefault")
    val sharedPrefBackPosterIsDefault: SharedPreferences,
    val api: MovieApi,
    val reviewDao: ReviewDao,
    private val movieRepository: MovieRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    val movies = MutableStateFlow<List<RatedMovieItem>>(emptyList())
    val favMovies = MutableStateFlow<MutableList<MovieItem>>(mutableListOf())
    val reviews = MutableStateFlow<MutableList<ReviewLocalItem>>(mutableListOf())
    val detailsForReview = MutableStateFlow<Map<Int, MovieDetailsForReviewItem>>(emptyMap())

    init {
        getFavMovies()
        getRatedMovies()
        getReviews()
    }

    private fun getFavMovies() {
        viewModelScope.launch {
            val response = movieRepository.getFavoriteMovies().toMutableList()
            favMovies.update { response }
        }
    }

    private fun getRatedMovies() {
        viewModelScope.launch {
            val response = movieRepository.getRatedMovies()
            movies.update { response }
        }
    }

    private fun getReviews() {
        viewModelScope.launch {
            val response = reviewRepository.getAllReviews().toMutableList()
            reviews.update { response }
        }
    }

    fun getDetails(movieId: Int) {
        viewModelScope.launch {
            val response = movieRepository.getMoviesDetails(movieId)
            detailsForReview.update { currentMap ->
                currentMap + (movieId to response)
            }
        }
    }
}