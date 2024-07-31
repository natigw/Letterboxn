package com.example.letterboxn.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieItem(
    val moviePoster : String?,
    val movieTitle : String,
    val movieDescription : String,
    val movieId : Int
) : Parcelable
