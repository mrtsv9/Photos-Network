package com.example.photosnetwork.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photosnetwork.domain.repository.main.MainRepository
import com.example.photosnetwork.domain.model.image.ImageItem
import com.example.photosnetwork.domain.model.image.PostImageItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private var _photo = Channel<Result<ImageItem>>()
    val photos = _photo.receiveAsFlow()

    fun postImage(postImageItem: PostImageItem) {
        viewModelScope.launch {
            val image = repository.postImage(postImageItem)
            _photo.send(image)
        }
    }

}