package com.example.letterboxn.domain.model

data class FirestoreUser (
    val username : String = "",
    val email : String = "",
    val password : String = "",
    val entrypin : String = "",
    val apikey : String = ""
)
