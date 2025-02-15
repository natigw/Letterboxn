package com.example.letterboxn.data.remote.model.temp


import com.google.gson.annotations.SerializedName

data class VenueLike(
    @SerializedName("venue")
    val venue: Int
)