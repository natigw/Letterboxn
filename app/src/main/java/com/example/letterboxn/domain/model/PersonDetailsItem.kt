package com.example.letterboxn.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PersonDetailsItem(
    val name : String,
    val image : String?,
    val department : String,
    val birthDay : String,
    val deathDay : String?,
    val linkToWebsite : String?,
    val biography : String
) : Parcelable
