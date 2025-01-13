package com.example.letterboxn.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxn.domain.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val reviewRepository : ReviewRepository
) : ViewModel(){

//    val reviews = MutableStateFlow<List<>>(emptyList())
//
//    init {
//        getReviews()
//    }
//
//    fun getReviews() {
//        viewModelScope.launch {
//            val response = reviewRepository.getReviews()
//            reviews.emit(response)
//        }
//    }
}