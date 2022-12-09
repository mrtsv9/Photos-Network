package com.example.photosnetwork.domain.model.auth

import com.example.photosnetwork.data.remote.dto.auth.UserAuthInputDto

data class UserAuthInput(
    val login: String,
    val password: String
)

fun UserAuthInput.toUserAuthInputDto(): UserAuthInputDto {
    return UserAuthInputDto(this.login, this.password)
}