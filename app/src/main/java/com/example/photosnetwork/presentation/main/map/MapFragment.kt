package com.example.photosnetwork.presentation.main.map

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withCreated
import com.example.photosnetwork.R
import com.example.photosnetwork.databinding.FragmentMapBinding
import com.example.photosnetwork.presentation.base.BaseFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>(), OnMapReadyCallback {

    override var hostActivity: Activity?
        get() = this.activity
        set(value) {}

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMapBinding
        get() = FragmentMapBinding::inflate

    private lateinit var map: GoogleMap

    private val viewModel by viewModels<MapViewModel>()

    override fun setup() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val marker = MarkerOptions().position(LatLng(53.897992848562836, 27.55186833757571))
        map.addMarker(marker)

        lifecycleScope.launch {
            viewModel.markers.collectLatest {
                it.forEach { mapMarker ->
                    val newMarker = MarkerOptions().position(LatLng(mapMarker.lat, mapMarker.lng))
                    map.addMarker(newMarker)
                }
            }
        }
        viewModel.getAllMarkers()
    }

}