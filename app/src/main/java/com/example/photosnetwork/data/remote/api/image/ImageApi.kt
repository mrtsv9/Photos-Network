package com.example.photosnetwork.data.remote.api.image

import com.example.photosnetwork.data.remote.dto.image.ImagesResponse
import com.example.photosnetwork.data.remote.dto.image.PostImageResponse
import com.example.photosnetwork.data.remote.dto.image.PostImageDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ImageApi {

    @GET("api/image")
    suspend fun getAllImages(
        @Header("Access-Token") token: String,
        @Query("page") page: Int,
    ): Response<ImagesResponse>

    @POST("api/image")
    suspend fun postImage(
        @Header("Access-Token") token: String,
        @Body postImageDto: PostImageDto,
    ): Response<PostImageResponse>

}