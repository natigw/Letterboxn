package com.example.letterboxn.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.letterboxn.data.local.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun addUser(user : UserEntity)

    @Delete
    suspend fun deleteUser(user : UserEntity)

    @Query("DELETE FROM UserEntity")
    suspend fun deleteAllUsers()

    @Query("SELECT * FROM UserEntity")
    suspend fun getAllUsers() : List<UserEntity>

    @Query("SELECT * FROM UserEntity")
    fun getAllUsersDynamically() : Flow<List<UserEntity>>

//    @Query("SELECT username FROM UserEntity")
//    suspend fun getAllUsernames() : List<UserEntity>

//    @Query("SELECT * FROM userentity WHERE username like :name")   //limit 1
//    suspend fun checkIfUserExists(name: String) : List<UserEntity> //UserEntity
}