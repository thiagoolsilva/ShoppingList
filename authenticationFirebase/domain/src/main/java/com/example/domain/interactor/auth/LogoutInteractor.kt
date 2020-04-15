/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.domain.interactor.auth

import com.example.domain.models.BasicUserInfo
import com.example.domain.repository.AuthenticationRepository

class LogoutInteractor constructor(private val authenticationRepository: AuthenticationRepository<BasicUserInfo>)   {

    suspend fun execute() {
        authenticationRepository.logout()
    }

}
