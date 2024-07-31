package com.example.letterboxn.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieBackPosterItem(
    val moviePoster : String?,
    val movieTitle : String,
    val movieBackPoster : String
) : Parcelable