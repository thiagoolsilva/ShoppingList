/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.presentation.di

import com.example.data.session.FirebaseAuthUserDataSource
import com.example.data.shoppinglist.FirebaseShoppingListDataSource
import com.example.domain.interactor.auth.GetLoggedUserInteractor
import com.example.domain.interactor.auth.LogoutInteractor
import com.example.domain.interactor.auth.SignInUserInteractor
import com.example.domain.interactor.auth.SignUpInteractor
import com.example.domain.repository.AuthenticationRepository
import com.example.domain.repository.ShoppingListRepository
import com.example.domain.shoppinglist.GetShoppingListsInteractor
import com.example.domain.shoppinglist.SaveShoppingListNameInteractor
import com.example.presentation.*
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val authenticationModule = module {

    single {
        FirebaseAuthUserDataSource()
    } bind AuthenticationRepository::class

    single {
        FirebaseShoppingListDataSource()
    } bind ShoppingListRepository::class

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

    factory {
        GetShoppingListsInteractor(shoppingListRepository = get(), authenticationRepository = get())
    }

    factory {
        SaveShoppingListNameInteractor(shoppingListRepository = get(), authenticationRepository = get())
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

    viewModel {
        ShoppingListViewModel(getShoppingListsInteractor = get())
    }

    viewModel {
        NewShoppingListViewModel(saveShoppingListNameInteractor = get())
    }

}