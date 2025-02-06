package com.example.letterboxn.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.letterboxn.data.local.database.review.ReviewDao
import com.example.letterboxn.data.remote.api.MovieApi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WriteReviewViewModel @Inject constructor(
    val api: MovieApi,
    val reviewDao: ReviewDao
) : ViewModel() {

}