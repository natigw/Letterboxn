package com.example.letterboxn.data.remote.model.movieCredit


import com.google.gson.annotations.SerializedName

data class MovieCreditItemDto(
    @SerializedName("cast")
    val cast: List<Cast>,
    @SerializedName("crew")
    val crew: List<Crew>,
    @SerializedName("id")
    val id: Int
)