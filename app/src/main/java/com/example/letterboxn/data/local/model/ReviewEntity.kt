package com.example.letterboxn.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ReviewEntity(
    val movieId : Int,
    val review : String,
    val rating : Float,
    val reviewDate : String,
    @PrimaryKey(autoGenerate = true) val reviewId : Int = 0
)
