package com.example.photosnetwork.data.local.entities.comment

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.photosnetwork.domain.model.comment.CommentItem

@Entity(tableName = "comment")
data class CommentEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val date: Long,
    val text: String,
    val imageId: Int,
)

fun CommentEntity.toCommentItem(): CommentItem {
    return CommentItem(id, date, text)
}
