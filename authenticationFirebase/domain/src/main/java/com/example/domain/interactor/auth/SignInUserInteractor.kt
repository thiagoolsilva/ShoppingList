/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.domain.interactor.auth

import com.example.domain.models.BasicUserInfoEntity
import com.example.domain.models.LoginParameterEntity
import com.example.domain.repository.AuthenticationRepository


class SignInUserInteractor constructor(private val authenticationRepository: AuthenticationRepository<BasicUserInfoEntity>) {

    suspend fun execute(parameterEntity: LoginParameterEntity): BasicUserInfoEntity? {
        return authenticationRepository.authenticateUser(parameterEntity.email, parameterEntity.password)
    }

}
