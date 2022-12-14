package com.example.photosnetwork.data.remote.repository.auth

import com.example.photosnetwork.data.remote.api.auth.LoginApi
import com.example.photosnetwork.data.remote.dto.auth.toUserAuthItem
import com.example.photosnetwork.domain.model.auth.UserAuthItem
import com.example.photosnetwork.domain.model.auth.UserAuthInput
import com.example.photosnetwork.domain.model.auth.toUserAuthInputDto
import com.example.photosnetwork.domain.repository.auth.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(private val api: LoginApi) : LoginRepository {

    override suspend fun logInUser(userAuthInput: UserAuthInput): UserAuthItem? {
        val response = api.userSignIn(userAuthInput.toUserAuthInputDto())
        return when (response.code()) {
            200 -> response.body()?.toUserAuthItem()
            400 -> null
            else -> null
        }
    }

}