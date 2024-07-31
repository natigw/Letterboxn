package com.example.letterboxn.data.remote.model.genre


import com.google.gson.annotations.SerializedName

data class GenreItemDto(
    @SerializedName("genres")
    val genres: List<Genre>
)