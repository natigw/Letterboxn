package com.example.letterboxn.domain.repository

import com.example.letterboxn.domain.model.ReviewLocalItem
import com.example.letterboxn.domain.model.home.recentReviews.ReviewWithMovieItem

interface ReviewRepository {

    suspend fun getAllReviews(): List<ReviewLocalItem>

    suspend fun getReviewsWithMovies(): List<ReviewWithMovieItem>

//    suspend fun getReviewsWithoutMovies() : List<ReviewWithoutMovieItem>

}