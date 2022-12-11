package com.example.photosnetwork.domain.model.comment

import com.example.photosnetwork.data.remote.dto.comment.PostCommentDto

data class PostCommentItem(
    val text: String
)

fun PostCommentItem.toPostCommentDto(): PostCommentDto {
    return PostCommentDto(text)
}