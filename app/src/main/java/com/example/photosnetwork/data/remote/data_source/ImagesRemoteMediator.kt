package com.example.photosnetwork.data.remote.data_source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.photosnetwork.data.local.dao.auth.UserDao
import com.example.photosnetwork.data.local.dao.image.ImageDao
import com.example.photosnetwork.data.local.entities.image.ImageEntity
import com.example.photosnetwork.data.remote.api.image.ImageApi
import com.example.photosnetwork.data.remote.dto.image.toImageEntityList
import javax.inject.Inject

@ExperimentalPagingApi
class ImagesRemoteMediator @Inject constructor(
    private val imageDao: ImageDao,
    private val userDao: UserDao,
    private val imageApi: ImageApi,
) : RemoteMediator<Int, ImageEntity>() {

    private var pageIndex = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ImageEntity>,
    ): MediatorResult {

        pageIndex =
            getPageIndex(loadType) ?: return MediatorResult.Success(endOfPaginationReached = true)

        val limit = state.config.pageSize

        return try {
            val images = fetchImages(pageIndex)
            if (loadType == LoadType.REFRESH) {
                imageDao.refresh(images)
            } else {
                imageDao.save(images)
            }
            MediatorResult.Success(
                endOfPaginationReached = images.size < limit
            )
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private fun getPageIndex(loadType: LoadType): Int? {
        pageIndex = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return null
            LoadType.APPEND -> ++pageIndex
        }
        return pageIndex
    }

    private suspend fun fetchImages(page: Int): List<ImageEntity> {
        val token = userDao.getUser()!!.token!!
        val response = imageApi.getAllImages(token, page)
        return if (!response.isSuccessful) emptyList()
        else response.body()!!.toImageEntityList()
    }

}