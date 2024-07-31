package com.example.letterboxn.domain.model

data class SearchItem(
    val movieId : Int,
    val movieTitle : String?,
    val moviePoster : String?,
    val movieRating : Float,
    val movieReleaseDate : String?,
    val mediaType : String?
)
