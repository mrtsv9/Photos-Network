package com.example.photosnetwork.data.local

import android.content.Context
import androidx.room.*
import com.example.photosnetwork.data.local.dao.auth.UserDao
import com.example.photosnetwork.data.local.dao.comment.CommentDao
import com.example.photosnetwork.data.local.dao.image.ImageDao
import com.example.photosnetwork.data.local.entities.auth.UserEntity
import com.example.photosnetwork.data.local.entities.comment.CommentEntity
import com.example.photosnetwork.data.local.entities.image.ImageEntity
import com.example.photosnetwork.domain.model.comment.CommentItem

@Database(
    entities = [
        UserEntity::class,
        ImageEntity::class,
        CommentEntity::class
    ],
    exportSchema = false,
    version = 2
)
abstract class AppDatabase : RoomDatabase() {

    abstract val userDao: UserDao
    abstract val imageDao: ImageDao
    abstract val commentDao: CommentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }

}