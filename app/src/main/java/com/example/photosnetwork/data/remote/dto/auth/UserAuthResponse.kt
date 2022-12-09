package com.example.photosnetwork.data.remote.dto.auth

import com.example.photosnetwork.domain.model.auth.UserAuthItem
import com.google.gson.annotations.SerializedName

data class UserAuthResponse(
    val status: Int?,
    @SerializedName("data")
    val authData: AuthData?
)

data class AuthData(
    val userId: Int?,
    val login: String?,
    val token: String?
)

fun UserAuthResponse.toUserAuthItem(): UserAuthItem {
    this.authData.let {
        return UserAuthItem(it?.userId, it?.login, it?.token)
    }
}