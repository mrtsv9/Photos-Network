package com.example.photosnetwork.di

import android.content.Context
import com.example.photosnetwork.data.local.AppDatabase
import com.example.photosnetwork.data.local.dao.AppDao
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
    fun provideAppDao(appDatabase: AppDatabase): AppDao = appDatabase.appDao

}