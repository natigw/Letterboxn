package com.example.letterboxn.data.remote.model.account.favoriteMovies


import com.google.gson.annotations.SerializedName

data class FavoriteMoviesDto(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<ResultFavoriteMovie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)