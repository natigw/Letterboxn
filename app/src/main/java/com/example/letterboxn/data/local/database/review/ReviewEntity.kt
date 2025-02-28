package com.example.letterboxn.data.local.database.review

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ReviewEntity(
    val movieId : Int,
    val review : String,
    val rating : Double,
    val reviewDate : String,
    @PrimaryKey(autoGenerate = true) val reviewId : Int = 0
)
