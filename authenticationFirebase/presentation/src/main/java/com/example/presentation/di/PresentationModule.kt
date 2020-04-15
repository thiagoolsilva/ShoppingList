/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.presentation.di

import com.example.data.session.FirebaseAuthUserDataSource
import com.example.domain.interactor.auth.GetLoggedUserInteractor
import com.example.domain.interactor.auth.LogoutInteractor
import com.example.domain.interactor.auth.SignInUserInteractor
import com.example.domain.interactor.auth.SignUpInteractor
import com.example.domain.repository.AuthenticationRepository
import com.example.presentation.LoggedViewModel
import com.example.presentation.NewAccountViewModel
import com.example.presentation.SignInViewModel
import com.example.presentation.SplashViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val authenticationModule = module {

    single {
        FirebaseAuthUserDataSource()
    } bind AuthenticationRepository::class

    factory {
        GetLoggedUserInteractor(authenticationRepository = get())
    }

    factory {
        SignInUserInteractor(authenticationRepository = get())
    }

    factory {
        LogoutInteractor(authenticationRepository = get())
    }

    factory {
        SignUpInteractor(authenticationRepository = get())
    }

    viewModel {
        SignInViewModel(signInUserInteractor = get())
    }

    viewModel {
        SplashViewModel(getLoggedUserInteractor = get())
    }

    viewModel {
        LoggedViewModel(getLoggedUserInteractor = get(), logoutInteractor = get())
    }

    viewModel {
        NewAccountViewModel(signUpInteractor = get())
    }

}