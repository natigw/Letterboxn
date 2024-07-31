package com.example.letterboxn.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.letterboxn.data.local.model.ReviewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {
    @Insert
    suspend fun addReview(review : ReviewEntity)

    @Delete
    suspend fun deleteReview(review : ReviewEntity)

    @Query("SELECT * FROM ReviewEntity")
    suspend fun getAllReviews() : List<ReviewEntity>

    @Query("SELECT * FROM ReviewEntity")
    fun getAllReviewsDynamically() : Flow<List<ReviewEntity>>

    @Query("SELECT reviewId FROM ReviewEntity WHERE movieId = :movieId AND review = :review")
    suspend fun getReviewId(movieId: Int, review: String): Int?
}