/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.presentation.di

import com.example.data.session.FirebaseAuthUserDataSource
import com.example.data.shoppinglist.FirebaseShoppingListDataSource
import com.example.domain.interactor.auth.GetLoggedUserInteractor
import com.example.domain.interactor.auth.LogoutInteractor
import com.example.domain.interactor.auth.SignInUserInteractor
import com.example.domain.interactor.auth.SignUpInteractor
import com.example.domain.interactor.auth.UpdateProfileInfoInteractor
import com.example.domain.repository.AuthenticationRepository
import com.example.domain.repository.ShoppingListRepository
import com.example.domain.shoppinglist.GetShoppingListItemsInteractor
import com.example.domain.shoppinglist.GetShoppingListsInteractor
import com.example.domain.shoppinglist.SaveShoppingListItemInteractor
import com.example.domain.shoppinglist.SaveShoppingListNameInteractor
import com.example.domain.shoppinglist.UpdateShoppingListItemInteractor
import com.example.presentation.LoginViewModel
import com.example.presentation.ProfileViewModel
import com.example.presentation.RegistrationViewModel
import com.example.presentation.ShoppingListItemViewModel
import com.example.presentation.ShoppingListViewModel
import com.example.presentation.NewShoppingListViewModel
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

    factory {
        UpdateProfileInfoInteractor(authenticationRepository = get())
    }

    viewModel {
        ShoppingListItemViewModel(getShoppingListItemsInteractor = get(), saveShoppingListItemInteractor = get(), updateShoppingListItemInteractor = get())
    }

    viewModel {
        LoginViewModel(signInUserInteractor = get(), logoutInteractor = get())
    }

    viewModel {
        ProfileViewModel(getLoggedUserInteractor = get(), updateProfileInfoInteractor = get())
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
