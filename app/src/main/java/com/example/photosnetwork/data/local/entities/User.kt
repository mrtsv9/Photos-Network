package com.example.photosnetwork.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1,
    val login: String?,
    val token: String?
)