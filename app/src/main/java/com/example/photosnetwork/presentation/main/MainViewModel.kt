package com.example.photosnetwork.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photosnetwork.data.local.dao.auth.UserDao
import com.example.photosnetwork.data.local.entities.auth.toUserAuthItem
import com.example.photosnetwork.domain.model.auth.UserAuthItem
import com.example.photosnetwork.domain.repository.main.MainRepository
import com.example.photosnetwork.domain.model.image.ImageItem
import com.example.photosnetwork.domain.model.image.PostImageItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    private val dao: UserDao,
) : ViewModel() {

    private var _photoResponseMessage = Channel<Result<Unit>>()
    val photoResponseMessage = _photoResponseMessage.receiveAsFlow()

    private var _user = Channel<UserAuthItem?>()
    val user = _user.receiveAsFlow()

    fun postImage(postImageItem: PostImageItem) {
        viewModelScope.launch {
            val image = repository.postImage(postImageItem)
            _photoResponseMessage.send(image)
        }
    }

    fun getUser() {
        viewModelScope.launch {
            _user.send(dao.getUser()?.toUserAuthItem())
        }
    }

}