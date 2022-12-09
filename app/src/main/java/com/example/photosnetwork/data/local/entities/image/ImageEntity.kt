package com.example.photosnetwork.data.local.entities.image

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.photosnetwork.domain.model.image.ImageItem

@Entity(tableName = "image")
data class ImageEntity(
    @PrimaryKey val id: Int,
    val url: String,
    val date: Int,
    val lat: Double,
    val lng: Double,
)

fun ImageEntity.toImageItem(): ImageItem {
    return ImageItem(id, url, date, lat, lng)
}