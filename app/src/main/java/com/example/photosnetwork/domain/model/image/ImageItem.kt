package com.example.photosnetwork.domain.model.image

import java.io.Serializable

data class ImageItem(
    val id: Int?,
    val url: String?,
    val date: Long?,
    val lat: Double?,
    val lng: Double?,
) : Serializable
