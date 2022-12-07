package com.example.photosnetwork.data.local

import android.content.Context
import androidx.room.*
import com.example.photosnetwork.data.local.dao.AppDao
import com.example.photosnetwork.data.local.entities.User

@Database(
    entities = [
        User::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract val appDao: AppDao

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