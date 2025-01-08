package com.example.letterboxn.domain.model.home.popularLists

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class PopularListItem (
    val listTitle: String = "Category title",
    val movie1 : ListItemEach,
    val movie2 : ListItemEach,
    val movie3 : ListItemEach,
    val movie4 : ListItemEach,
    val listAuthorName : String,
    val listAuthorImage : String?,
    val likeCount: Int,
    val commentCount: Int
) : Parcelable