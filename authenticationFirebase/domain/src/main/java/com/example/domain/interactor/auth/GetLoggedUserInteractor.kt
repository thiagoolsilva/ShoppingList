/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.domain.interactor.auth

import com.example.domain.models.BasicUserInfoEntity
import com.example.domain.repository.AuthenticationRepository

class GetLoggedUserInteractor constructor(private val authenticationRepository: AuthenticationRepository<BasicUserInfoEntity>) {

    suspend fun execute(): BasicUserInfoEntity? {
        return authenticationRepository.currentUser()
    }

}

