package com.example.photosnetwork.data.remote.data_source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.photosnetwork.data.remote.api.ImageApi
import com.example.photosnetwork.domain.model.image.ImageItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

const val NETWORK_PAGE_SIZE = 20

class ImageDataSource @Inject constructor(private val api: ImageApi) {

    fun getImages(): Flow<PagingData<ImageItem>> {
        return Pager(config = PagingConfig(pageSize = NETWORK_PAGE_SIZE,
            enablePlaceholders = true), pagingSourceFactory = {
            ImageRemotePagingSource(api)
        }).flow
    }

}