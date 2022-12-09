package com.example.photosnetwork.data.local.entities.auth

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1,
    val login: String?,
    val token: String?
)