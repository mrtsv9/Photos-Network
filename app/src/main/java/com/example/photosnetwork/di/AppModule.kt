package com.example.photosnetwork.di

import com.example.photosnetwork.data.remote.api.LoginApi
import com.example.photosnetwork.data.remote.api.RegisterApi
import com.example.photosnetwork.data.remote.repository.LoginRepositoryImpl
import com.example.photosnetwork.data.remote.repository.RegisterRepositoryImpl
import com.example.photosnetwork.domain.repository.LoginRepository
import com.example.photosnetwork.domain.repository.RegisterRepository
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
    fun provideRegisterRepository(api: RegisterApi): RegisterRepository = RegisterRepositoryImpl(api)

}