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
import com.example.domain.shoppinglist.*
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

    factory {
        GetShoppingListItemsInteractor(authenticationRepository = get(), shoppingListRepository = get())
    }

    factory {
        SaveShoppingListItemInteractor(shoppingListRepository = get(), authenticationRepository = get())
    }

    factory {
        UpdateShoppingListItemInteractor(authenticationRepository = get(), shoppingListRepository = get())
    }

    viewModel {
        ShoppingListItemViewModel(getShoppingListItemsInteractor = get(), saveShoppingListItemInteractor = get(), updateShoppingListItemInteractor = get())
    }

    viewModel {
        LoginViewModel(signInUserInteractor = get(), logoutInteractor = get())
    }

    viewModel {
        SplashViewModel(getLoggedUserInteractor = get())
    }

    viewModel {
        LoggedViewModel(getLoggedUserInteractor = get(), logoutInteractor = get())
    }

    viewModel {
        RegistrationViewModel(signUpInteractor = get())
    }

    viewModel {
        ShoppingListViewModel(getShoppingListsInteractor = get())
    }

    viewModel {
        NewShoppingListViewModel(saveShoppingListNameInteractor = get())
    }

}