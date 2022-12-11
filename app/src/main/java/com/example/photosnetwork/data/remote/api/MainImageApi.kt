package com.example.photosnetwork.data.remote.api

import com.example.photosnetwork.data.remote.dto.image.PostImageDto
import com.example.photosnetwork.data.remote.dto.image.PostImageResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface MainImageApi {

    @POST("api/image")
    suspend fun postImage(
        @Header("Access-Token") token: String,
        @Body postImageDto: PostImageDto,
    ): Response<PostImageResponse>

}