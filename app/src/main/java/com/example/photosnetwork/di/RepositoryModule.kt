package com.example.photosnetwork.di

import com.example.photosnetwork.data.remote.repository.comment.CommentRepositoryImpl
import com.example.photosnetwork.domain.repository.comment.CommentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindCommentRepository(commentRepository: CommentRepositoryImpl): CommentRepository

}