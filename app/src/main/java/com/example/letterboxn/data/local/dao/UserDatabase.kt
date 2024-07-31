package com.example.letterboxn.data.local.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.letterboxn.data.local.model.UserEntity

@Database([UserEntity::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun getUserDao() : UserDao
}