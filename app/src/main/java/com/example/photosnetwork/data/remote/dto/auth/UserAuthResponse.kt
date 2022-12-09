package com.example.photosnetwork.data.remote.dto.auth

import com.example.photosnetwork.domain.model.auth.UserAuthItem

data class UserAuthResponse(
    val status: Int?,
    val authData: AuthData?
)

data class AuthData(
    val userId: Int?,
    val login: String?,
    val token: String?
)

fun UserAuthResponse.toUserAuthItem(): UserAuthItem {
    this.authData.let {
        return UserAuthItem(it?.userId?.toInt(), it?.login, it?.token)
    }
}