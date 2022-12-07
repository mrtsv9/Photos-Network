package com.example.photosnetwork.domain.model

import com.example.photosnetwork.data.remote.dto.UserAuthInputDto

data class UserAuthInput(
    val login: String,
    val password: String
)

fun UserAuthInput.toUserAuthInputDto(): UserAuthInputDto {
    return UserAuthInputDto(this.login, this.password)
}