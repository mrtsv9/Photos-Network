package com.example.photosnetwork.domain.model.image

data class ImageItem(
    val id: Int,
    val url: String,
    val date: Int,
    val lat: Double,
    val lng: Double,
)
