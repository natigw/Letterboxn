package com.example.letterboxn.domain.model

data class MovieDetailsForReviewItem (
    val movieId : Int,
    val movieTitle : String,
    val moviePoster : String?,
    val movieRating : Float,
    val movieReleaseDate : String
)