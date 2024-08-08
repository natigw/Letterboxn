package com.example.letterboxn.data.remote.repository

import com.example.letterboxn.data.local.database.review.ReviewDao
import com.example.letterboxn.domain.model.ReviewLocalItem
import com.example.letterboxn.domain.repository.ReviewRepository
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    val reviewDao : ReviewDao
): ReviewRepository {
    override suspend fun getAllReviews(): List<ReviewLocalItem> {
        val response = reviewDao.getAllReviews()
        return response.map {
            ReviewLocalItem(
                movieId = it.movieId,
                review = it.review,
                rating = it.rating,
                reviewDate = it.reviewDate,
                reviewId = it.reviewId
            )
        }.reversed()
    }
}