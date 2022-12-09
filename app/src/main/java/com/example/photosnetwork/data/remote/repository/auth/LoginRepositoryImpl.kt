package com.example.photosnetwork.data.remote.repository.auth

import android.util.Log
import com.example.photosnetwork.data.remote.api.LoginApi
import com.example.photosnetwork.data.remote.dto.auth.toUserAuthItem
import com.example.photosnetwork.domain.model.auth.UserAuthItem
import com.example.photosnetwork.domain.model.auth.UserAuthInput
import com.example.photosnetwork.domain.model.auth.toUserAuthInputDto
import com.example.photosnetwork.domain.repository.auth.LoginRepository
import com.example.photosnetwork.util.Constants.TAG
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(private val api: LoginApi) : LoginRepository {

    override suspend fun logInUser(userAuthInput: UserAuthInput): UserAuthItem? {
        val response = api.userSignIn(userAuthInput.toUserAuthInputDto())
        Log.d(TAG, "logInUser: ${response.body()?.toUserAuthItem()}")
        return when (response.code()) {
            200 -> response.body()?.toUserAuthItem()
            400 -> null
            else -> null
        }
    }

}