package com.example.letterboxn.data.remote.model.account.watchlist.addWatchlist


import com.google.gson.annotations.SerializedName

data class ResponseAddRemoveWatchlist(
    @SerializedName("status_code")
    val statusCode: Int,
    @SerializedName("status_message")
    val statusMessage: String,
    @SerializedName("success")
    val success: Boolean
)