package com.example.photosnetwork.domain.model.image

data class PostImageItem(
    val base64Image: String,
    val date: Long,
    val lat: Double,
    val lng: Double,
)
