package com.example.photosnetwork.data.remote.repository.image

import android.util.Log
import androidx.paging.PagingData
import com.example.photosnetwork.data.remote.api.ImageApi
import com.example.photosnetwork.data.remote.data_source.ImageDataSource
import com.example.photosnetwork.data.remote.dto.image.PostImageResponse
import com.example.photosnetwork.data.remote.dto.image.PostImageDto
import com.example.photosnetwork.data.remote.dto.image.toImageItemList
import com.example.photosnetwork.domain.model.image.ImageItem
import com.example.photosnetwork.domain.repository.image.ImageRepository
import com.example.photosnetwork.util.Constants.TAG
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val api: ImageApi,
    private val dataSource: ImageDataSource
): ImageRepository {

    override suspend fun getImagesPaginated(token: String, page: Int): List<ImageItem>? {
        val response = api.getAllImages(token, page)
        Log.d(TAG, "getAllImages: ${response.body().toString()}")
        return response.body()?.toImageItemList()
    }

    override fun getImagesPagingData(): Flow<PagingData<ImageItem>> {
        return dataSource.getImages()
    }

    override suspend fun postImage(token: String, postImageDto: PostImageDto): PostImageResponse? {
        val response = api.postImage(token, postImageDto)
        if (response.isSuccessful) {
            Log.d(TAG, "postImage: ${response.body()}")
            return response.body()
        }
        return null
    }

}