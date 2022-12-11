package com.example.photosnetwork.data.remote.data_source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.photosnetwork.data.local.dao.auth.UserDao
import com.example.photosnetwork.data.local.dao.comment.CommentDao
import com.example.photosnetwork.data.local.entities.comment.CommentEntity
import com.example.photosnetwork.data.remote.api.comment.CommentApi
import com.example.photosnetwork.data.remote.dto.comment.toCommentEntityList
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

@ExperimentalPagingApi
class CommentsRemoteMediator @AssistedInject constructor(
    private val commentDao: CommentDao,
    private val userDao: UserDao,
    private val commentApi: CommentApi,
    @Assisted private val imageId: String,
) : RemoteMediator<Int, CommentEntity>() {

    private var pageIndex = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CommentEntity>,
    ): MediatorResult {

        pageIndex =
            getPageIndex(loadType) ?: return MediatorResult.Success(endOfPaginationReached = true)

        val limit = state.config.pageSize

        return try {
            val comments = fetchImages(imageId, pageIndex)
            if (loadType == LoadType.REFRESH) {
                commentDao.refresh(comments ?: emptyList())
            } else {
                commentDao.save(comments ?: emptyList())
            }
            MediatorResult.Success(
                endOfPaginationReached = comments!!.size < limit
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

    private suspend fun fetchImages(path: String, page: Int): List<CommentEntity>? {
        val token = userDao.getUser()!!.token!!
        val response = commentApi.getAllComments(token, path, page)
        return if (!response.isSuccessful) emptyList()
        else response.body()?.toCommentEntityList(imageId.toInt())
    }

    @AssistedFactory
    interface Factory {
        fun create(imageId: String): CommentsRemoteMediator
    }

}