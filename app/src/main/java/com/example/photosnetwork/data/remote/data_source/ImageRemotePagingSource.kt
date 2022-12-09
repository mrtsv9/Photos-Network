package com.example.photosnetwork.data.remote.data_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.photosnetwork.data.remote.api.ImageApi
import com.example.photosnetwork.data.remote.dto.image.ImagesResponse
import com.example.photosnetwork.data.remote.dto.image.toImageItemList
import com.example.photosnetwork.domain.model.image.ImageItem
import com.example.photosnetwork.domain.repository.image.ImageRepository
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

const val IMAGES_START_INDEX_PAGE = 0

class ImageRemotePagingSource(private val api: ImageApi) :
    PagingSource<Int, ImageItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageItem> {
        val pageIndex = params.key ?: IMAGES_START_INDEX_PAGE
        return try {
            val imageList = api.getAllImages(
                // TODO ADD TOKEN
                "n4cDD1ZhvH8ZyqdfFonUluT6r18fDKabufk4KzXs8GteK7WeIEtSy41Jx8eSP1w4",
                page = pageIndex
            )
            val nextKey =
                if (imageList.body()!!.data!!.isEmpty()) {
                    null
                } else {
                    // By default, initial load size = 3 * NETWORK PAGE SIZE
                    // ensure we're not requesting duplicating items at the 2nd request
                    pageIndex + (params.loadSize / NETWORK_PAGE_SIZE)
                }
            LoadResult.Page(
                data = imageList.body()!!.toImageItemList(),
                prevKey = if (pageIndex == IMAGES_START_INDEX_PAGE) null else pageIndex,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ImageItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}