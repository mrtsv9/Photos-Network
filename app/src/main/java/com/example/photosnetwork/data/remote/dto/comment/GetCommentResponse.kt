package com.example.photosnetwork.data.remote.dto.comment


import com.example.photosnetwork.data.local.entities.comment.CommentEntity
import com.example.photosnetwork.domain.model.comment.CommentItem
import com.google.gson.annotations.SerializedName

data class GetCommentResponse(
    @SerializedName("status")
    val status: Int?,
    @SerializedName("data")
    val data: List<CommentData?>?,
) {
    data class CommentData(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("date")
        val date: Long?,
        @SerializedName("text")
        val text: String?,
    )
}

fun GetCommentResponse.toCommentItemsList(): List<CommentItem>? {
    return this.data?.map {
        it!!.toCommentItem()
    }
}

private fun GetCommentResponse.CommentData.toCommentItem(): CommentItem {
    return CommentItem(id.toString(), date!!, text!!)
}

fun GetCommentResponse.toCommentEntityList(imageId: Int): List<CommentEntity>? {
    return this.data?.map {
        it!!.toCommentEntity(imageId)
    }
}

private fun GetCommentResponse.CommentData.toCommentEntity(imageId: Int): CommentEntity {
    return CommentEntity(id.toString(), date!!, text!!, imageId)
}