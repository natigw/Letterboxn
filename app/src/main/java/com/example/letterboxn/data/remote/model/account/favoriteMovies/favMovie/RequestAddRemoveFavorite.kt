package com.example.letterboxn.data.remote.model.account.favoriteMovies.favMovie


import com.google.gson.annotations.SerializedName

data class RequestAddRemoveFavorite(
    @SerializedName("favorite")
    val favorite: Boolean,
    @SerializedName("media_id")
    val mediaId: Int,
    @SerializedName("media_type")
    val mediaType: String
)