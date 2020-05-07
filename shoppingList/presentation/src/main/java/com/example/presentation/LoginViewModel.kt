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

package com.example.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.interactor.auth.LogoutInteractor
import com.example.domain.interactor.auth.SignInUserInteractor
import com.example.domain.models.LoginParameterEntity
import com.example.presentation.model.UserInfoView
import com.example.presentation.model.ViewState
import com.example.presentation.util.toUserInfoView
import com.example.shared.exception.InvalidUserPassword
import com.example.shared.exception.UserNotFound
import com.example.shared.exception.UserNotLogged
import kotlinx.coroutines.launch
import timber.log.Timber

class LoginViewModel constructor(
    private val signInUserInteractor: SignInUserInteractor,
    private val logoutInteractor: LogoutInteractor
) :
    ViewModel() {

    sealed class AuthenticationState {
        class AuthenticatedUser(val userInfoView: UserInfoView?) : AuthenticationState()
        class InvalidFields(val fields: List<Pair<String, String>>) : AuthenticationState()
        object UserDisconnected : AuthenticationState()
        object UnauthorizedUser : AuthenticationState()
        object UserNotFound : AuthenticationState()
        object InvalidUserPassword : AuthenticationState()
    }

    companion object {
        val INPUT_USERNAME = "INPUT_USERNAME" to "empty username"
        val INPUT_PASSWORD = "INPUT_PASSWORD" to "empty password"
    }

    private val _currentLiveState = MutableLiveData<ViewState<AuthenticationState>>()
    val currentLiveState: LiveData<ViewState<AuthenticationState>>
        get() = _currentLiveState

    /**
     * Gets authenticate user
     * @param email user's email
     * @param password user's password
     */
    fun authenticateUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                if (validateFields(email, password)) {
                    // send loading information to view
                    _currentLiveState.value = ViewState(ViewState.Status.LOADING)

                    // authenticate provided user
                    val authenticatedUser = signInUserInteractor.execute(LoginParameterEntity(email, password))?.toUserInfoView()
                    val authenticationState = AuthenticationState.AuthenticatedUser(authenticatedUser)

                    // send userInfo feedback to view
                    _currentLiveState.value = ViewState(ViewState.Status.SUCCESS, authenticationState)
                }
            } catch (error: Exception) {
                Timber.e(error)

                when (error) {
                    is UserNotLogged -> _currentLiveState.value = ViewState(ViewState.Status.SUCCESS, data = AuthenticationState.UnauthorizedUser)
                    is UserNotFound -> _currentLiveState.value = ViewState(ViewState.Status.SUCCESS, data = AuthenticationState.UserNotFound)
                    is InvalidUserPassword -> _currentLiveState.value = ViewState(ViewState.Status.SUCCESS, data = AuthenticationState.InvalidUserPassword)
                    else -> _currentLiveState.value = ViewState(ViewState.Status.ERROR, error = error)
                }
            }
        }
    }

    /**
     * Disconnect current User
     */
    fun disconnect() {
        viewModelScope.launch {
            try {
                // send loading information to view
                _currentLiveState.value = ViewState(ViewState.Status.LOADING)

                logoutInteractor.execute()

                _currentLiveState.value = ViewState(ViewState.Status.SUCCESS, AuthenticationState.UserDisconnected)
            } catch (error: Exception) {
                _currentLiveState.value = ViewState(ViewState.Status.ERROR, error = error)
            }
        }
    }

    /**
     * Validate required fields
     */
    private fun validateFields(login: String, password: String): Boolean {
        val invalidFields = arrayListOf<Pair<String, String>>()

        if (login.isEmpty()) {
            invalidFields.add(INPUT_USERNAME)
        }

        if (password.isEmpty()) {
            invalidFields.add(INPUT_PASSWORD)
        }

        if (invalidFields.isNotEmpty()) {
            _currentLiveState.value = ViewState(ViewState.Status.ERROR, data = AuthenticationState.InvalidFields(invalidFields))
            return false
        }

        return true
    }
}
