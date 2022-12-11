package com.example.photosnetwork.domain.repository.main

import com.example.photosnetwork.data.remote.dto.image.PostImageDto
import com.example.photosnetwork.domain.model.image.ImageItem
import com.example.photosnetwork.domain.model.image.PostImageItem

interface MainRepository {

    suspend fun postImage(postImageItem: PostImageItem): Result<Unit>

}