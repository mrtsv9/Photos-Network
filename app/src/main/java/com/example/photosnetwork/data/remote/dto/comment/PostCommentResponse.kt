package com.example.photosnetwork.data.remote.dto.comment

import com.example.photosnetwork.domain.model.comment.CommentItem
import com.google.gson.annotations.SerializedName
import java.util.*

data class PostCommentResponse(
    val status: Int,
    @SerializedName("data")
    val commentData: CommentData,
)

data class CommentData(
    val id: String,
    val date: Long,
    val text: String
)

fun PostCommentResponse.toCommentItem(): CommentItem {
    return CommentItem(commentData.id, commentData.date, commentData.text)
}