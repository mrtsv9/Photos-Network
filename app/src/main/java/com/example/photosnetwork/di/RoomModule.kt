package com.example.photosnetwork.di

import android.content.Context
import com.example.photosnetwork.data.local.AppDatabase
import com.example.photosnetwork.data.local.dao.auth.UserDao
import com.example.photosnetwork.data.local.dao.image.ImageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): UserDao = appDatabase.userDao

    @Provides
    @Singleton
    fun provideImageDao(appDatabase: AppDatabase): ImageDao = appDatabase.imageDao

}