package com.example.letterboxn.data.remote.model.account.watchlist


import com.google.gson.annotations.SerializedName

data class WatchlistDto(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<ResultWatchlist>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)