package com.example.photosnetwork.di

import com.example.photosnetwork.data.remote.RetrofitInstance
import com.example.photosnetwork.data.remote.api.ImageApi
import com.example.photosnetwork.data.remote.api.LoginApi
import com.example.photosnetwork.data.remote.api.RegisterApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
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
    fun provideRegisterApi(retrofit: Retrofit): RegisterApi = retrofit.create(RegisterApi::class.java)

    @Provides
    fun provideImageApi(retrofit: Retrofit): ImageApi = retrofit.create(ImageApi::class.java)

}