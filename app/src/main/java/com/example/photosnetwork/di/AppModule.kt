package com.example.photosnetwork.di

import androidx.paging.ExperimentalPagingApi
import com.example.photosnetwork.data.local.dao.auth.UserDao
import com.example.photosnetwork.data.local.dao.comment.CommentDao
import com.example.photosnetwork.data.local.dao.image.ImageDao
import com.example.photosnetwork.data.remote.api.image.ImageApi
import com.example.photosnetwork.data.remote.api.auth.LoginApi
import com.example.photosnetwork.data.remote.api.main.MainImageApi
import com.example.photosnetwork.data.remote.api.auth.RegisterApi
import com.example.photosnetwork.data.remote.api.comment.CommentApi
import com.example.photosnetwork.data.remote.data_source.CommentsRemoteMediator
import com.example.photosnetwork.data.remote.data_source.ImagesRemoteMediator
import com.example.photosnetwork.data.remote.repository.auth.LoginRepositoryImpl
import com.example.photosnetwork.data.remote.repository.auth.RegisterRepositoryImpl
import com.example.photosnetwork.data.remote.repository.comment.CommentRepositoryImpl
import com.example.photosnetwork.data.remote.repository.image.ImageRepositoryImpl
import com.example.photosnetwork.data.remote.repository.main.MainRepositoryImpl
import com.example.photosnetwork.data.remote.repository.map.MapRepositoryImpl
import com.example.photosnetwork.domain.repository.auth.LoginRepository
import com.example.photosnetwork.domain.repository.auth.RegisterRepository
import com.example.photosnetwork.domain.repository.comment.CommentRepository
import com.example.photosnetwork.domain.repository.image.ImageRepository
import com.example.photosnetwork.domain.repository.main.MainRepository
import com.example.photosnetwork.domain.repository.map.MapRepository
import dagger.Binds
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
    ): ImageRepository = ImageRepositoryImpl(api, imageDao, imagesRemoteMediator)

    @Provides
    fun provideMainRepository(api: MainImageApi, dao: UserDao): MainRepository =
        MainRepositoryImpl(api, dao)

    @Provides
    fun provideMapRepository(api: ImageApi, userDao: UserDao): MapRepository =
        MapRepositoryImpl(api, userDao)
}