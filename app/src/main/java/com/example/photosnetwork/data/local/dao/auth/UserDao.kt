package com.example.photosnetwork.data.local.dao.auth

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.photosnetwork.data.local.entities.auth.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity)

    @Query("SELECT * from user where id = 1")
    suspend fun getUser(): UserEntity?

    @Query("delete from user where id = 1")
    suspend fun deleteUser()

}