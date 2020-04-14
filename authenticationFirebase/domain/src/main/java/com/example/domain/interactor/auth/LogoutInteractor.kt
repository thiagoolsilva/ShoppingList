/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.domain.interactor.auth

import com.example.domain.repository.AuthenticationRepository

class LogoutInteractor constructor(private val authenticationRepository: AuthenticationRepository<Void>)   {

    suspend fun execute() {
        authenticationRepository.logout()
    }

}
