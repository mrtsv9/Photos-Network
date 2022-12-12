package com.example.photosnetwork.presentation.main.image

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.photosnetwork.domain.model.comment.CommentItem
import com.example.photosnetwork.domain.model.image.ImageItem
import com.example.photosnetwork.domain.repository.image.ImageRepository
import com.example.photosnetwork.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(private val repository: ImageRepository) : ViewModel() {

    private var _deleteImage = Channel<Resource<Unit>>()
    val deleteImage = _deleteImage.receiveAsFlow()

    fun getPhotosPagingData(): Flow<PagingData<ImageItem>> {
        return repository.getImagesPagingData().cachedIn(viewModelScope)
    }

    fun deletePhoto(id: Int) {
        viewModelScope.launch {
            _deleteImage.send(repository.deleteImage(id))
        }
    }

}