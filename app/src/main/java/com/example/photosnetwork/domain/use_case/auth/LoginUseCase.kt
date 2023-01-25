package com.example.photosnetwork.domain.use_case.auth

import com.example.photosnetwork.data.local.dao.auth.UserDao
import com.example.photosnetwork.data.local.entities.auth.UserEntity
import com.example.photosnetwork.domain.model.auth.UserAuthItem
import com.example.photosnetwork.domain.model.auth.UserAuthInput
import com.example.photosnetwork.domain.repository.auth.LoginRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: LoginRepository,
    private val dao: UserDao
) {

    suspend operator fun invoke(userAuthInput: UserAuthInput): UserAuthItem? {
        val userAuthData = repository.logInUser(userAuthInput) ?: return null
        dao.insertUser(UserEntity(1, userAuthData.login, userAuthData.token))
        return userAuthData
    }

}