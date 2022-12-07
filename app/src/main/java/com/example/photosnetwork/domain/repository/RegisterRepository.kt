package com.example.photosnetwork.domain.repository

import com.example.photosnetwork.domain.model.UserAuthData
import com.example.photosnetwork.domain.model.UserAuthInput

interface RegisterRepository {

    suspend fun registerUser(userAuthInput: UserAuthInput): UserAuthData?

}