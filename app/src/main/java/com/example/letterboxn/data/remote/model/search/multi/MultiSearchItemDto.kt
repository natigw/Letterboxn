package com.example.letterboxn.data.remote.model.search.multi


import com.google.gson.annotations.SerializedName

data class MultiSearchItemDto(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<ResultMultiSearch>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)