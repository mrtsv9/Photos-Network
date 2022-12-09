package com.example.photosnetwork.domain.use_case.auth

import com.example.photosnetwork.data.local.dao.AppDao
import com.example.photosnetwork.data.local.entities.User
import com.example.photosnetwork.domain.model.auth.UserAuthItem
import com.example.photosnetwork.domain.model.auth.UserAuthInput
import com.example.photosnetwork.domain.repository.auth.LoginRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: LoginRepository,
    private val dao: AppDao,
) {

    suspend operator fun invoke(userAuthInput: UserAuthInput): UserAuthItem? {
        val userAuthData = repository.logInUser(userAuthInput) ?: return null
        dao.insertUser(User(1, userAuthData.login, userAuthData.login))
        return userAuthData
    }

}