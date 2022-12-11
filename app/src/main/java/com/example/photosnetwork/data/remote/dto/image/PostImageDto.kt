package com.example.photosnetwork.data.remote.dto.image

import com.example.photosnetwork.domain.model.image.PostImageItem

data class PostImageDto(
    val base64Image: String?,
    val date: Long?,
    val lat: Double?,
    val lng: Double?,
)

