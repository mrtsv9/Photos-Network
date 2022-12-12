package com.example.photosnetwork.domain.repository.image

import androidx.paging.PagingData
import com.example.photosnetwork.data.remote.dto.image.ImagesResponse
import com.example.photosnetwork.data.remote.dto.image.PostImageResponse
import com.example.photosnetwork.data.remote.dto.image.PostImageDto
import com.example.photosnetwork.domain.model.image.ImageItem
import com.example.photosnetwork.util.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface ImageRepository {

    suspend fun getImagesPaginated(token: String, page: Int): List<ImageItem>?

    fun getImagesPagingData(): Flow<PagingData<ImageItem>>

    suspend fun postImage(token: String, postImageDto: PostImageDto): PostImageResponse?

    suspend fun deleteImage(id: Int): Resource<Unit>
}