package com.example.letterboxn.data.local.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.letterboxn.data.local.model.ReviewEntity

@Database([ReviewEntity::class], version = 1)
abstract class ReviewDatabase : RoomDatabase() {
    abstract fun getReviewDao() : ReviewDao
}