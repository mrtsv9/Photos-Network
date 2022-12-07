package com.example.photosnetwork.data.remote.api

import com.example.photosnetwork.data.remote.dto.UserAuthInputDto
import com.example.photosnetwork.data.remote.dto.UserAuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    @POST("api/account/signin")
    suspend fun userSignIn(@Body body: UserAuthInputDto): Response<UserAuthResponse>

}