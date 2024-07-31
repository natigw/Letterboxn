package com.example.letterboxn.data.remote.model.review.details


import com.google.gson.annotations.SerializedName

data class AuthorDetails(
    @SerializedName("avatar_path")
    val avatarPath: Any,
    @SerializedName("name")
    val name: String,
    @SerializedName("rating")
    val rating: Any,
    @SerializedName("username")
    val username: String
)