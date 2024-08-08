package com.example.letterboxn.data.local.database.review

import androidx.room.Database
import androidx.room.RoomDatabase

@Database([ReviewEntity::class], version = 1)
abstract class ReviewDatabase : RoomDatabase() {
    abstract fun getReviewDao() : ReviewDao
}