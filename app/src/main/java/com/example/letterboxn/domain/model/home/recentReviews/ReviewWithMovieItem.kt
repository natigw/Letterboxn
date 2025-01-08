package com.example.letterboxn.domain.model.home.recentReviews

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ReviewWithMovieItem (
    val authorName : String,
    val authorImage : String?,
    val review : String?,
    val reviewRating : Double,
    val commentCount : Int,
    val movieId : Int,
    val movieTitle : String,
    val moviePoster : String?,
    val movieRating : Double,
    val movieReleaseDate : String,
) : Parcelable