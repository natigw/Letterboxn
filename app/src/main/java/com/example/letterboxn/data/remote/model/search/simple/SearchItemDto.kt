package com.example.letterboxn.data.remote.model.search.simple


import com.google.gson.annotations.SerializedName

data class SearchItemDto(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<ResultSearch>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)