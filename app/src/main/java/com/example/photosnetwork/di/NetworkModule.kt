package com.example.photosnetwork.di

import com.example.photosnetwork.data.remote.RetrofitInstance
import com.example.photosnetwork.data.remote.api.image.ImageApi
import com.example.photosnetwork.data.remote.api.auth.LoginApi
import com.example.photosnetwork.data.remote.api.main.MainImageApi
import com.example.photosnetwork.data.remote.api.auth.RegisterApi
import com.example.photosnetwork.data.remote.api.comment.CommentApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = RetrofitInstance.getRetrofitInstance()

    @Provides
    fun provideLoginApi(retrofit: Retrofit): LoginApi = retrofit.create(LoginApi::class.java)

    @Provides
    fun provideRegisterApi(retrofit: Retrofit): RegisterApi =
        retrofit.create(RegisterApi::class.java)

    @Provides
    fun provideImageApi(retrofit: Retrofit): ImageApi = retrofit.create(ImageApi::class.java)

    @Provides
    fun provideMainApi(retrofit: Retrofit): MainImageApi = retrofit.create(MainImageApi::class.java)

    @Provides
    fun provideCommentApi(retrofit: Retrofit): CommentApi = retrofit.create(CommentApi::class.java)

}