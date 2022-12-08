package com.example.photosnetwork.presentation.splash_screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photosnetwork.domain.model.UserAuthData
import com.example.photosnetwork.domain.model.UserAuthInput
import com.example.photosnetwork.domain.repository.LoginRepository
import com.example.photosnetwork.domain.use_case.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {

    private val _userAuthData = Channel<UserAuthData>()
    val userAuthData = _userAuthData.receiveAsFlow()

    private val _isErrorOccurred = MutableSharedFlow<Boolean>()
    val isErrorOccurred = _isErrorOccurred.asSharedFlow()

    fun logInUser(userAuthInput: UserAuthInput) {
        viewModelScope.launch(Dispatchers.IO) {
            val userAuthData = loginUseCase(userAuthInput)
            if (userAuthData == null) _isErrorOccurred.emit(true)
            else _userAuthData.send(userAuthData)
        }
    }

}