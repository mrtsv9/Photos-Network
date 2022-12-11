package com.example.photosnetwork.domain.model.image

import com.example.photosnetwork.data.remote.dto.image.PostImageDto

data class PostImageItem(
    val base64Image: String,
    val date: Long,
    val lat: Double,
    val lng: Double,
)

fun PostImageItem.toPostImageDto(): PostImageDto {
    return PostImageDto(base64Image, date, lat, lng)
}
