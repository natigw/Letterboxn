package com.example.letterboxn.data.remote.model.review


import com.google.gson.annotations.SerializedName

data class MovieReviewDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<ResultMovieReview>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)