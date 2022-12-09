package com.example.photosnetwork.data.remote.repository.auth

import com.example.photosnetwork.data.remote.api.RegisterApi
import com.example.photosnetwork.data.remote.dto.auth.toUserAuthItem
import com.example.photosnetwork.domain.model.auth.UserAuthItem
import com.example.photosnetwork.domain.model.auth.UserAuthInput
import com.example.photosnetwork.domain.model.auth.toUserAuthInputDto
import com.example.photosnetwork.domain.repository.auth.RegisterRepository
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(private val api: RegisterApi) :
    RegisterRepository {

    override suspend fun registerUser(userAuthInput: UserAuthInput): UserAuthItem? {
        val response = api.userSignUp(userAuthInput.toUserAuthInputDto())
        return when (response.code()) {
            200 -> response.body()?.toUserAuthItem()
            400 -> null
            else -> null
        }
    }

}