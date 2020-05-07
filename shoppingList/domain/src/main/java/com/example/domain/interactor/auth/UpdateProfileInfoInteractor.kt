/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.domain.interactor.auth

import com.example.domain.models.BasicUserInfoEntity
import com.example.domain.repository.AuthenticationRepository

class UpdateProfileInfoInteractor(private val authenticationRepository: AuthenticationRepository<BasicUserInfoEntity>) {

    /**
     * update user name
     */
    suspend fun execute(name: String) {
        authenticationRepository.updateProfile(name)
    }
}