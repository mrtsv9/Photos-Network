package com.example.photosnetwork.data.remote.dto.image

import com.example.photosnetwork.domain.model.image.ImageItem
import com.example.photosnetwork.domain.model.image.PostImageItem

data class PostImageResponse(
    val id: Int?,
    val url: String?,
    val date: Int?,
    val lat: Double?,
    val lng: Double?,
)

fun PostImageResponse.toPostImageItem(): ImageItem {
    return ImageItem(id, url, date?.toLong(), lat, lng)
}