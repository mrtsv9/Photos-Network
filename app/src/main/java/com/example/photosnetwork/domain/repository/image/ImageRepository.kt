package com.example.photosnetwork.domain.repository.image

import androidx.paging.PagingData
import com.example.photosnetwork.data.remote.dto.image.ImagesResponse
import com.example.photosnetwork.data.remote.dto.image.PostImageResponse
import com.example.photosnetwork.data.remote.dto.image.PostImageDto
import com.example.photosnetwork.domain.model.image.ImageItem
import kotlinx.coroutines.flow.Flow

interface ImageRepository {

    suspend fun getImagesPaginated(token: String, page: Int): List<ImageItem>?

    fun getImagesPagingData(): Flow<PagingData<ImageItem>>

    suspend fun postImage(token: String, postImageDto: PostImageDto): PostImageResponse?
}