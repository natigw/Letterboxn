package com.example.letterboxn.domain.model.home.popularLists

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListItemEach(
//    val movieId : Int,
    val movieTitle : String,
    val moviePoster : String?
) : Parcelable