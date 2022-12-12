package com.example.photosnetwork.data.remote.dto.image

data class PostImageDto(
    val base64Image: String?,
    val date: Long?,
    val lat: Double?,
    val lng: Double?,
)

