package com.example.photosnetwork.presentation.splash_screen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photosnetwork.domain.model.UserAuthData
import com.example.photosnetwork.domain.model.UserAuthInput
import com.example.photosnetwork.domain.repository.RegisterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: RegisterRepository): ViewModel() {

    private val _userAuthData = Channel<UserAuthData>()
    val userAuthData = _userAuthData.receiveAsFlow()

    private val _isErrorOccurred = MutableSharedFlow<Boolean>()
    val isErrorOccurred = _isErrorOccurred.asSharedFlow()

    fun registerUser(userAuthInput: UserAuthInput) {
        viewModelScope.launch {
            val userAuthData = repository.registerUser(userAuthInput)
            if (userAuthData == null) _isErrorOccurred.emit(true)
            else _userAuthData.send(userAuthData)
        }
    }

}