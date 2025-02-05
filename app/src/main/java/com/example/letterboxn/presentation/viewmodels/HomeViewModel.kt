package com.example.letterboxn.presentation.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxn.domain.model.MovieItem
import com.example.letterboxn.domain.model.home.popularLists.PopularListItem
import com.example.letterboxn.domain.model.home.recentReviews.ReviewWithMovieItem
import com.example.letterboxn.domain.repository.MovieRepository
import com.example.letterboxn.domain.repository.ReviewRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class HomeViewModel @Inject constructor(
    @Named("UserLoggedIn")
    val sharedPrefLoggedIn: SharedPreferences,
    @Named("UserStatusInApp")
    val sharedPrefStatus: SharedPreferences,
    @Named("UserProfileImage")
    val sharedPrefProfilePicture: SharedPreferences,
    val firestore: FirebaseFirestore,
    private val movieRepository: MovieRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    val popularMovies = MutableStateFlow<List<MovieItem>>(emptyList())
    val popularLists = MutableStateFlow<List<PopularListItem>>(emptyList())
    val recentReviews = MutableStateFlow<List<ReviewWithMovieItem>>(emptyList())

    val userName = sharedPrefLoggedIn.getString("username", null)
    val userEmail = sharedPrefLoggedIn.getString("email", null)
    val status = sharedPrefStatus.getBoolean("status", false)
    val followerCount = 22   //from api
    val followingCount = 39

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

    suspend fun checkIfUserExists(email: String): Boolean {
        return try {
            val documentSnapshot = firestore.collection("users").document(email).get().await()
            documentSnapshot.exists()
        } catch (e: Exception) {
            false
        }
    }
}