package com.example.letterboxn.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ReviewWithoutMovieItem (
    val reviewId : String,
    val authorName : String,
    val authorImage : String?,
    val review : String,
    val reviewRating : Float,
    val commentCount : Int
) : Parcelable