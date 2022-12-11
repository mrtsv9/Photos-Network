package com.example.photosnetwork.data.local.dao.image

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.photosnetwork.data.local.entities.image.ImageEntity

@Dao
interface ImageDao {

    @Query("select * from image ")
    fun getImagesPagingSource(): PagingSource<Int, ImageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(images: List<ImageEntity>)

//    @Query("delete from image")
//    suspend fun clear()

    @Transaction
    suspend fun refresh(images: List<ImageEntity>) {
//        clear()
        save(images)
    }
}