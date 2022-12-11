package com.example.photosnetwork.domain.repository.comment

import androidx.paging.PagingData
import com.example.photosnetwork.domain.model.comment.PostCommentItem
import com.example.photosnetwork.domain.model.comment.CommentItem
import com.example.photosnetwork.domain.model.image.ImageItem
import com.example.photosnetwork.util.Resource
import kotlinx.coroutines.flow.Flow

interface CommentRepository {

    suspend fun postComment(postCommentItem: PostCommentItem, imageId: String): Resource<CommentItem>

    fun getCommentsPagingData(imageId: String): Flow<PagingData<CommentItem>>

}