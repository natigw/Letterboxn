package com.example.letterboxn.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class PopularListItem (
    val movie1 : ListItemEach,
    val movie2 : ListItemEach,
    val movie3 : ListItemEach,
    val movie4 : ListItemEach,
    val authorName : String,
    val authorImage : String?
) : Parcelable