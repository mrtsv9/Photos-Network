package com.example.photosnetwork.data.remote.dto.image

import com.example.photosnetwork.domain.model.image.ImageItem
import com.google.gson.annotations.SerializedName

data class ImagesResponse(
    @SerializedName("status")
    val status: Int?,
    @SerializedName("data")
    val data: List<ImageData?>?,
)  {
    data class ImageData(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("url")
        val url: String?,
        @SerializedName("date")
        val date: Int?,
        @SerializedName("lat")
        val lat: Double?,
        @SerializedName("lng")
        val lng: Double?,
    )
}

fun ImagesResponse.toImageItemList(): List<ImageItem> {
    return this.data?.map {
        it!!.toImageItem()
    } ?: emptyList()
}

private fun ImagesResponse.ImageData.toImageItem(): ImageItem {
    return ImageItem(id!!, url!!, date!!, lat!!, lng!!)
}