package com.example.letterboxn.data.remote.model.account.ratedMovies.rateMovie


import com.google.gson.annotations.SerializedName

data class RequestAddRating(
    @SerializedName("value")
    val value: Float
)