package com.example.photosnetwork.data.remote.dto

import com.example.photosnetwork.domain.model.UserAuthInput

data class UserAuthInputDto(
    var login: String,
    val password: String
)
