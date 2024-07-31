package com.example.letterboxn.data.remote.model.popularList


import com.google.gson.annotations.SerializedName

data class PopularListItemDto(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<ResultPopularList>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)