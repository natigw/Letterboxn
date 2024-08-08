package com.example.letterboxn.data.local.database.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    val username : String,
    @PrimaryKey val email : String,
    val password : String,
    val pin : Int
    //val apiKey : String
)
