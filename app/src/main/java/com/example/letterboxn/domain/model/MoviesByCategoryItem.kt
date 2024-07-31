package com.example.letterboxn.domain.model

data class MoviesByCategoryItem (
    val movieId : Int,
    val movieTitle : String,
    val moviePoster : String?,
    val rating : Float,
    val likeCount : Int
)