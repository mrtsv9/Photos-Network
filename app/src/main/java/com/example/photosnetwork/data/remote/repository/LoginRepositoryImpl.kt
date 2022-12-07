package com.example.photosnetwork.data.remote.repository

import com.example.photosnetwork.data.remote.api.LoginApi
import com.example.photosnetwork.data.remote.dto.toUserAuthData
import com.example.photosnetwork.domain.model.UserAuthData
import com.example.photosnetwork.domain.model.UserAuthInput
import com.example.photosnetwork.domain.model.toUserAuthInputDto
import com.example.photosnetwork.domain.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(private val api: LoginApi) : LoginRepository {

    override suspend fun logInUser(userAuthInput: UserAuthInput): UserAuthData? {
        val response = api.userSignIn(userAuthInput.toUserAuthInputDto())
        return when (response.code()) {
            200 -> response.body()?.toUserAuthData()
            400 -> null
            else -> null
        }
    }

}