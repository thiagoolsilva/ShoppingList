/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.domain.interactor.auth

import androidx.lifecycle.MutableLiveData
import com.example.domain.interactor.BaseInteractor
import com.example.domain.models.BasicUserInfo
import com.example.domain.models.LoginParameter
import com.example.domain.repository.AuthenticationRepository

class SignUpInteractor constructor(val authenticationRepository: AuthenticationRepository<BasicUserInfo>) :
    BaseInteractor<LoginParameter, BasicUserInfo?> {

    override fun execute(
        parameter: LoginParameter,
        result: MutableLiveData<Result<BasicUserInfo?>>
    ) {
        try {
            val authenticatedUser =
                authenticationRepository.signUp(parameter.email, parameter.password)

            result.postValue(Result.success(authenticatedUser))

        } catch (error: Exception) {
            result.postValue(Result.failure(error))
        }
    }

}

