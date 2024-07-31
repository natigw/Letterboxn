package com.example.letterboxn.domain.model

data class UserReviewItem (
    val reviewId : Int,
    val movieId : Int,
    val moviePoster : String?,
    val review : String,
    val rating : Float,
    val reviewDate : String
)