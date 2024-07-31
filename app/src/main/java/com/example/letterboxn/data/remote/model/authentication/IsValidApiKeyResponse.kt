package com.example.letterboxn.data.remote.model.authentication


import com.google.gson.annotations.SerializedName

data class IsValidApiKeyResponse(
    @SerializedName("status_code")
    val statusCode: Int,
    @SerializedName("status_message")
    val statusMessage: String,
    @SerializedName("success")
    val success: Boolean
)