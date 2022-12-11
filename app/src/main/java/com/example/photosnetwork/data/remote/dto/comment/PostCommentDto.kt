package com.example.photosnetwork.data.remote.dto.comment

import com.example.photosnetwork.domain.model.comment.PostCommentItem

data class PostCommentDto(
    val text: String
)

fun PostCommentDto.toPostCommentItem(): PostCommentItem {
    return PostCommentItem(text)
}