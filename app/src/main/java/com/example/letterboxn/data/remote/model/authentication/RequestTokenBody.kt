package com.example.letterboxn.data.remote.model.authentication

import com.google.gson.annotations.SerializedName

data class RequestTokenBody(
    @SerializedName("request_token")
    val requestToken: String
)
