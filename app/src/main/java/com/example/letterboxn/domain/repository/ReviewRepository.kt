package com.example.letterboxn.domain.repository

import com.example.letterboxn.domain.model.ReviewLocalItem

interface ReviewRepository {
    suspend fun getAllReviews() : List<ReviewLocalItem>
}