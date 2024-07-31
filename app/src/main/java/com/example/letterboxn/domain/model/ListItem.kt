package com.example.letterboxn.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListItem(
    val movieItems : List<MovieItem>,
    val listTitle : String,
    val authorName : String,
    val authorImage : String?,
    val likeCount : Int,
    val commentCount: Int,
) : Parcelable
