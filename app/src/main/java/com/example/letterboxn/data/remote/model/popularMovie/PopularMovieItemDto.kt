package com.example.letterboxn.data.remote.model.popularMovie


import com.google.gson.annotations.SerializedName

data class PopularMovieItemDto(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<ResultPopularMovie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)