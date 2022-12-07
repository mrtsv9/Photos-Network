package com.example.photosnetwork.data.remote.api

import com.example.photosnetwork.data.remote.dto.UserAuthInputDto
import com.example.photosnetwork.data.remote.dto.UserAuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterApi {

    @POST("api/account/signup")
    suspend fun userSignUp(@Body body: UserAuthInputDto): Response<UserAuthResponse>

}