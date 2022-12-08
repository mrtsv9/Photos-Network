package com.example.photosnetwork.domain.use_case

import com.example.photosnetwork.data.local.dao.AppDao
import com.example.photosnetwork.data.local.entities.User
import com.example.photosnetwork.domain.model.UserAuthData
import com.example.photosnetwork.domain.model.UserAuthInput
import com.example.photosnetwork.domain.repository.RegisterRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: RegisterRepository,
    private val dao: AppDao,
) {

    suspend operator fun invoke(userAuthInput: UserAuthInput): UserAuthData? {
        val userAuthData = repository.registerUser(userAuthInput) ?: return null
        dao.insertUser(User(1, userAuthData.login, userAuthData.login))
        return userAuthData
    }

}