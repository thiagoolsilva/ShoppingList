/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.domain.interactor.auth

import com.example.domain.interactor.SimpleInteractor
import com.example.domain.repository.AuthenticationRepository

class LogoutInteractor constructor(val authenticationRepository: AuthenticationRepository<Void>) :
    SimpleInteractor<Unit> {

    override fun execute() {
        authenticationRepository.logout()
    }

}
