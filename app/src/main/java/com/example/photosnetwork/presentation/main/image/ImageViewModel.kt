package com.example.photosnetwork.presentation.main.image

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.photosnetwork.data.remote.dto.image.ImagesResponse
import com.example.photosnetwork.data.remote.dto.image.PostImageDto
import com.example.photosnetwork.domain.model.image.ImageItem
import com.example.photosnetwork.domain.repository.image.ImageRepository
import com.example.photosnetwork.util.Constants.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(private val repository: ImageRepository) : ViewModel() {

    private var _photos = Channel<List<ImageItem>>()
    val photos = _photos.receiveAsFlow()

    fun getAllPhotos(token: String, page: Int) {
        viewModelScope.launch {
            val response = repository.getImagesPaginated(token, page)
            Log.d(TAG, "getAllPhotos: ${response.toString()}")
            if (response != null) _photos.send(response)
        }
    }

    fun getPhotosPagingData(): Flow<PagingData<ImageItem>> {
        return repository.getImagesPagingData().cachedIn(viewModelScope)
    }

    fun postImage(token: String, postImageDto: PostImageDto) {
        viewModelScope.launch {
            val response = repository.postImage(token, postImageDto)
            Log.d(TAG, "postImage view model: $response")
        }
    }

}