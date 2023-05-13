package com.example.photosnetwork.domain.use_case.auth

import com.example.photosnetwork.data.local.dao.auth.UserDao
import com.example.photosnetwork.data.local.entities.auth.UserEntity
import com.example.photosnetwork.domain.model.auth.UserAuthItem
import com.example.photosnetwork.domain.model.auth.UserAuthInput
import com.example.photosnetwork.domain.repository.auth.RegisterRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: RegisterRepository,
    private val dao: UserDao,
) {

    suspend operator fun invoke(userAuthInput: UserAuthInput): UserAuthItem? {
        val userAuthData = repository.registerUser(userAuthInput) ?: return null
        dao.insertUser(UserEntity(1, userAuthData.login, userAuthData.token))
        return userAuthData
    }

}