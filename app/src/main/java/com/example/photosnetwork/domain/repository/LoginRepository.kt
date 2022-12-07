package com.example.photosnetwork.domain.repository

import com.example.photosnetwork.domain.model.UserAuthData
import com.example.photosnetwork.domain.model.UserAuthInput
import retrofit2.Response

interface LoginRepository {

    suspend fun logInUser(userAuthInput: UserAuthInput): UserAuthData?

}