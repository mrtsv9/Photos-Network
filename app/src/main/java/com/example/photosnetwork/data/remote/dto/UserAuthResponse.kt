package com.example.photosnetwork.data.remote.dto

import com.example.photosnetwork.domain.model.UserAuthData

data class UserAuthResponse(
    val status: Int?,
    val data: Data?
)

data class Data(
    val userId: Int?,
    val login: String?,
    val token: String?
)

fun UserAuthResponse.toUserAuthData(): UserAuthData {
    this.data.let {
        return UserAuthData(it?.userId?.toInt(), it?.login, it?.token)
    }
}