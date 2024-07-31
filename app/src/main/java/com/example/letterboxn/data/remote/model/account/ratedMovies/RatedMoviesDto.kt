package com.example.letterboxn.data.remote.model.account.ratedMovies


import com.google.gson.annotations.SerializedName

data class RatedMoviesDto(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<ResultRatedMovie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)