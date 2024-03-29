package com.example.photosnetwork.data.remote.repository.map

import com.example.photosnetwork.data.local.dao.auth.UserDao
import com.example.photosnetwork.data.remote.api.image.ImageApi
import com.example.photosnetwork.data.remote.dto.image.toMarkerList
import com.example.photosnetwork.domain.model.map.MapMarkerItem
import com.example.photosnetwork.domain.repository.map.MapRepository
import javax.inject.Inject

class MapRepositoryImpl @Inject constructor(private val api: ImageApi, val userDao: UserDao) :
    MapRepository {

    override suspend fun getAllMarkers(): List<MapMarkerItem> {
        val markerList = mutableListOf<MapMarkerItem>()
        val token = userDao.getUser()?.token
        if (token != null) {
            var counter = 0
            var response = api.getAllImages(token, counter)
            while (response.body()?.data!!.isNotEmpty()) {
                response = api.getAllImages(token, counter)
                counter++
                markerList.addAll(response.body()?.toMarkerList()!!)
            }
            return markerList
        }
        return emptyList()
    }

}
