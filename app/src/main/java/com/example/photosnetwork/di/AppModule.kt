package com.example.photosnetwork.di

import androidx.paging.ExperimentalPagingApi
import com.example.photosnetwork.data.local.dao.image.ImageDao
import com.example.photosnetwork.data.remote.api.ImageApi
import com.example.photosnetwork.data.remote.api.LoginApi
import com.example.photosnetwork.data.remote.api.RegisterApi
import com.example.photosnetwork.data.remote.data_source.ImagesRemoteMediator
import com.example.photosnetwork.data.remote.repository.auth.LoginRepositoryImpl
import com.example.photosnetwork.data.remote.repository.auth.RegisterRepositoryImpl
import com.example.photosnetwork.data.remote.repository.image.ImageRepositoryImpl
import com.example.photosnetwork.domain.repository.auth.LoginRepository
import com.example.photosnetwork.domain.repository.auth.RegisterRepository
import com.example.photosnetwork.domain.repository.image.ImageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideLoginRepository(api: LoginApi): LoginRepository = LoginRepositoryImpl(api)

    @Provides
    fun provideRegisterRepository(api: RegisterApi): RegisterRepository =
        RegisterRepositoryImpl(api)

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    fun provideImageRepository(
        api: ImageApi,
        imageDao: ImageDao,
        imagesRemoteMediator: ImagesRemoteMediator,
    ): ImageRepository =
        ImageRepositoryImpl(api, imageDao, imagesRemoteMediator)
}