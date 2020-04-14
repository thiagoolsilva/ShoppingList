/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.presentation.di

import com.example.data.session.FirebaseAuthUserDataSource
import com.example.domain.interactor.auth.SignInUserInteractor
import com.example.domain.repository.AuthenticationRepository
import com.example.presentation.auth.SignInViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val authenticationModule = module {

    single {
        FirebaseAuthUserDataSource()
    } bind AuthenticationRepository::class

    factory {
        SignInUserInteractor(authenticationRepository = get())
    }

    viewModel {
        SignInViewModel(signInUserInteractor = get())
    }

}