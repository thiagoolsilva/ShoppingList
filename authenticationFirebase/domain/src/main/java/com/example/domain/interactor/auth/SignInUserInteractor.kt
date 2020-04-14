/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.domain.interactor.auth

import com.example.domain.models.BasicUserInfo
import com.example.domain.models.LoginParameter
import com.example.domain.repository.AuthenticationRepository


class SignInUserInteractor constructor(private val authenticationRepository: AuthenticationRepository<BasicUserInfo>) {

    suspend fun execute(parameter: LoginParameter): BasicUserInfo? {
        return authenticationRepository.authenticateUser(parameter.email, parameter.password)
    }

}
