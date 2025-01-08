package com.example.letterboxn.domain.model

data class ReviewLocalItem (
    val movieId : Int,
    val review : String,
    val rating : Double,
    val reviewDate : String,
    val reviewId : Int = 0
)