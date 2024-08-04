package com.example.letterboxn.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieItem(
    val movieId : Int,
    val movieTitle : String,
    val moviePoster : String?,
    val movieDescription : String
) : Parcelable
