package com.example.photosnetwork.data.local.dao.comment

import androidx.paging.PagingSource
import androidx.room.*
import com.example.photosnetwork.data.local.entities.comment.CommentEntity

@Dao
interface CommentDao {

    @Query("select * from comment where imageId = :imageId")
    fun getCommentsPagingSource(imageId: Int): PagingSource<Int, CommentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(images: List<CommentEntity>)

    @Query("delete from comment where imageId > 0")
    suspend fun clear()

    @Transaction
    suspend fun refresh(images: List<CommentEntity>) {
        clear()
        save(images)
    }

}