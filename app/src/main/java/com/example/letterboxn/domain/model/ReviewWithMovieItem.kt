package com.example.letterboxn.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ReviewWithMovieItem (
    val authorName : String,
    val authorImage : String?,
    val review : String,
    val reviewRating : Float,
    val commentCount : Int,
    val movieId : Int,
    val movieTitle : String,
    val moviePoster : String?,
    val movieRating : Float,
    val movieReleaseDate : String,
) : Parcelable