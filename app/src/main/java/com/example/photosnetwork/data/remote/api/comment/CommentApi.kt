package com.example.photosnetwork.data.remote.api.comment

import com.example.photosnetwork.data.remote.dto.comment.GetCommentResponse
import com.example.photosnetwork.data.remote.dto.comment.PostCommentDto
import com.example.photosnetwork.data.remote.dto.comment.PostCommentResponse
import retrofit2.Response
import retrofit2.http.*

interface CommentApi {

    @POST("api/image/{imageId}/comment")
    suspend fun postComment(
        @Header("Access-Token") token: String,
        @Body postCommentDto: PostCommentDto,
        @Path("imageId") imageId: String,
    ): Response<PostCommentResponse>

    @GET("/api/image/{imageId}/comment")
    suspend fun getAllComments(
        @Header("Access-Token") token: String,
        @Path("imageId") imageId: String,
        @Query("page") page: Int,
    ): Response<GetCommentResponse>

    @DELETE("/api/image/{imageId}/comment/{commentId}")
    suspend fun deleteImage(
        @Header("Access-Token") token: String,
        @Path("imageId") imageId: Int,
        @Path("commentId") commentId: Int,
    ): Response<Unit>

}