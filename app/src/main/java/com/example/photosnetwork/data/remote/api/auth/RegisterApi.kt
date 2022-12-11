package com.example.photosnetwork.data.remote.api.auth

import com.example.photosnetwork.data.remote.dto.auth.UserAuthInputDto
import com.example.photosnetwork.data.remote.dto.auth.UserAuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterApi {

    @POST("api/account/signup")
    suspend fun userSignUp(@Body body: UserAuthInputDto): Response<UserAuthResponse>

}