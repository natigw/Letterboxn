package com.example.letterboxn.data.remote.model.authentication


import com.google.gson.annotations.SerializedName

data class ResponseSession(
    @SerializedName("session_id")
    val sessionId: String,
    @SerializedName("success")
    val success: Boolean
)