package com.example.photosnetwork.data.remote.repository.main

import com.example.photosnetwork.data.local.dao.auth.UserDao
import com.example.photosnetwork.data.remote.api.MainImageApi
import com.example.photosnetwork.data.remote.dto.image.PostImageResponse
import com.example.photosnetwork.data.remote.dto.image.toPostImageItem
import com.example.photosnetwork.domain.model.image.ImageItem
import com.example.photosnetwork.domain.model.image.PostImageItem
import com.example.photosnetwork.domain.model.image.toPostImageDto
import com.example.photosnetwork.domain.repository.main.MainRepository
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val api: MainImageApi,
    private val dao: UserDao,
) : MainRepository {
    override suspend fun postImage(postImageItem: PostImageItem): Result<Unit> {
        val token = dao.getUser()?.token
        if (token != null) {
            val response = api.postImage(token, postImageItem.toPostImageDto())
            return when (response.code()) {
                200 -> {
                    Result.success(response.body()!!)
                }
                400 -> {
                    Result.failure(Throwable("Bad image"))
                }
                500 -> {
                    Result.failure(Throwable("File upload error"))
                }
                else -> {
                    Result.failure(Throwable("Unknown error occurred"))
                }
            }
        } else return Result.failure(Throwable("No token provided"))
    }
}