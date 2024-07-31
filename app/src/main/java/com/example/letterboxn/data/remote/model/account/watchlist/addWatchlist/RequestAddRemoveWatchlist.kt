package com.example.letterboxn.data.remote.model.account.watchlist.addWatchlist


import com.google.gson.annotations.SerializedName

data class RequestAddRemoveWatchlist(
    @SerializedName("media_id")
    val mediaId: Int,
    @SerializedName("media_type")
    val mediaType: String,
    @SerializedName("watchlist")
    val watchlist: Boolean
)