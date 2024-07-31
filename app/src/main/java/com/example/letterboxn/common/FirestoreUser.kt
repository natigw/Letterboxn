package com.example.letterboxn.common

data class FirestoreUser (
    val username : String = "",
    val email : String = "",
    val password : String = "",
    val entrypin : String = "",
    val apikey : String = ""
)
