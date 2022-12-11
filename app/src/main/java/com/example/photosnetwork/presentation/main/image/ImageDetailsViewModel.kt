package com.example.photosnetwork.presentation.main.image

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.photosnetwork.domain.model.comment.PostCommentItem
import com.example.photosnetwork.domain.model.comment.CommentItem
import com.example.photosnetwork.domain.model.image.ImageItem
import com.example.photosnetwork.domain.repository.comment.CommentRepository
import com.example.photosnetwork.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageDetailsViewModel @Inject constructor(private val repository: CommentRepository) :
    ViewModel() {

    private var _postComment = Channel<Resource<CommentItem>>()
    val postComment = _postComment.receiveAsFlow()



    fun postComment(postCommentItem: PostCommentItem, imageId: String) {
        viewModelScope.launch {
            val comment = repository.postComment(postCommentItem, imageId)
            _postComment.send(comment)
        }
    }

    fun getPhotosPagingData(imageId: String): Flow<PagingData<CommentItem>> {
        return repository.getCommentsPagingData(imageId).cachedIn(viewModelScope)
    }

}