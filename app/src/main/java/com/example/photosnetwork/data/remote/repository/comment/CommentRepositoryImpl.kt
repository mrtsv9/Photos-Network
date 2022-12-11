package com.example.photosnetwork.data.remote.repository.comment

import androidx.paging.*
import com.example.photosnetwork.data.local.dao.auth.UserDao
import com.example.photosnetwork.data.local.dao.comment.CommentDao
import com.example.photosnetwork.data.local.entities.comment.toCommentItem
import com.example.photosnetwork.data.local.entities.image.toImageItem
import com.example.photosnetwork.data.remote.api.comment.CommentApi
import com.example.photosnetwork.data.remote.data_source.CommentsRemoteMediator
import com.example.photosnetwork.data.remote.dto.comment.toCommentItem
import com.example.photosnetwork.domain.model.comment.PostCommentItem
import com.example.photosnetwork.domain.model.comment.CommentItem
import com.example.photosnetwork.domain.model.comment.toPostCommentDto
import com.example.photosnetwork.domain.model.image.ImageItem
import com.example.photosnetwork.domain.repository.comment.CommentRepository
import com.example.photosnetwork.util.Constants
import com.example.photosnetwork.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CommentRepositoryImpl @OptIn(ExperimentalPagingApi::class) @Inject constructor(
    private val api: CommentApi,
    private val userDao: UserDao,
    private val commentDao: CommentDao,
    private val remoteMediator: CommentsRemoteMediator.Factory,
) : CommentRepository {

    override suspend fun postComment(
        postCommentItem: PostCommentItem,
        imageId: String,
    ): Resource<CommentItem> {
        val token = userDao.getUser()?.token
        if (token != null) {
            return try {
                val response = api.postComment(token, postCommentItem.toPostCommentDto(), imageId)
                if (response.isSuccessful) {
                    Resource.Success(response.body()!!.toCommentItem())
                } else Resource.Error("Internal server error")
            } catch (e: Exception) {
                Resource.Error("No internet connection")
            }
        }
        return Resource.Error("Unknown error occurred")
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getCommentsPagingData(imageId: String): Flow<PagingData<CommentItem>> {
        return Pager(config = PagingConfig(pageSize = Constants.NETWORK_PAGE_SIZE),
            remoteMediator = remoteMediator.create(imageId),
            pagingSourceFactory = { commentDao.getCommentsPagingSource(imageId.toInt()) }
        ).flow.map { pagingData ->
            pagingData.map {
                it.toCommentItem()
            }
        }
    }

}