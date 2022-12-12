package com.example.photosnetwork.presentation.main.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photosnetwork.domain.model.image.ImageItem
import com.example.photosnetwork.domain.model.map.MapMarkerItem
import com.example.photosnetwork.domain.repository.map.MapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(private val repository: MapRepository) : ViewModel() {

    private var _markers = Channel<List<MapMarkerItem>>()
    val markers = _markers.receiveAsFlow()

    fun getAllMarkers() {
        viewModelScope.launch {
            _markers.send(repository.getAllMarkers())
        }
    }

}