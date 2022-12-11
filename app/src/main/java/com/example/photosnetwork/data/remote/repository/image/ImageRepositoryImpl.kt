package com.example.photosnetwork.data.remote.repository.image

import android.util.Log
import androidx.paging.*
import com.example.photosnetwork.data.local.dao.image.ImageDao
import com.example.photosnetwork.data.local.entities.image.toImageItem
import com.example.photosnetwork.data.remote.api.image.ImageApi
import com.example.photosnetwork.data.remote.data_source.ImagesRemoteMediator
import com.example.photosnetwork.data.remote.dto.image.PostImageResponse
import com.example.photosnetwork.data.remote.dto.image.PostImageDto
import com.example.photosnetwork.data.remote.dto.image.toImageItemList
import com.example.photosnetwork.domain.model.image.ImageItem
import com.example.photosnetwork.domain.repository.image.ImageRepository
import com.example.photosnetwork.util.Constants.NETWORK_PAGE_SIZE
import com.example.photosnetwork.util.Constants.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalPagingApi
@Singleton
class ImageRepositoryImpl @Inject constructor(
    private val api: ImageApi,
    private val imageDao: ImageDao,
    private val remoteMediator: ImagesRemoteMediator,
) : ImageRepository {

    override suspend fun getImagesPaginated(token: String, page: Int): List<ImageItem>? {
        val response = api.getAllImages(token, page)
        return response.body()?.toImageItemList()
    }

    override fun getImagesPagingData(): Flow<PagingData<ImageItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE),
            remoteMediator = remoteMediator,
            pagingSourceFactory = { imageDao.getImagesPagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { it.toImageItem() }
        }
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