package com.example.letterboxn.data.remote.model.person


import com.google.gson.annotations.SerializedName

data class PersonItemDto(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val resultPeople: List<ResultPerson>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)