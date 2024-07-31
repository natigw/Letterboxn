package com.example.letterboxn.data.remote.model.account.accountDetails


import com.google.gson.annotations.SerializedName

data class Gravatar(
    @SerializedName("hash")
    val hash: String
)