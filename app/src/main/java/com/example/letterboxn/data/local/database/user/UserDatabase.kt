package com.example.letterboxn.data.local.database.user

import androidx.room.Database
import androidx.room.RoomDatabase

@Database([UserEntity::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun getUserDao() : UserDao
}