package com.example.letterboxn.domain.model

data class WatchlistItem(
    val movieId : Int,
    val movieTitle : String,
    val moviePoster : String?,
    val likeCount : Int,
    val rating : Float,
    val releaseDate : String,
)
