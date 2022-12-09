package com.example.photosnetwork.domain.repository.auth

import com.example.photosnetwork.domain.model.auth.UserAuthItem
import com.example.photosnetwork.domain.model.auth.UserAuthInput

interface LoginRepository {

    suspend fun logInUser(userAuthInput: UserAuthInput): UserAuthItem?

}