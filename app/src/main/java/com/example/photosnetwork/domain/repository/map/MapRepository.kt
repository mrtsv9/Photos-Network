package com.example.photosnetwork.domain.repository.map

import com.example.photosnetwork.domain.model.image.ImageItem
import com.example.photosnetwork.domain.model.map.MapMarkerItem

interface MapRepository {

    suspend fun getAllMarkers(): List<MapMarkerItem>

}