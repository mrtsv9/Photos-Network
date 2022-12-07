package com.example.photosnetwork.data.remote.repository

import com.example.photosnetwork.data.remote.api.RegisterApi
import com.example.photosnetwork.data.remote.dto.toUserAuthData
import com.example.photosnetwork.domain.model.UserAuthData
import com.example.photosnetwork.domain.model.UserAuthInput
import com.example.photosnetwork.domain.model.toUserAuthInputDto
import com.example.photosnetwork.domain.repository.RegisterRepository
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(private val api: RegisterApi) : RegisterRepository {

    override suspend fun registerUser(userAuthInput: UserAuthInput): UserAuthData? {
        val response = api.userSignUp(userAuthInput.toUserAuthInputDto())
        return when (response.code()) {
            200 -> response.body()?.toUserAuthData()
            400 -> null
            else -> null
        }
    }

}