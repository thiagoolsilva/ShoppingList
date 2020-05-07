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
import com.example.domain.interactor.auth.SignUpInteractor
import com.example.domain.models.BasicUserInfoEntity
import com.example.domain.models.LoginParameterEntity
import com.example.presentation.model.ViewState
import com.example.shared.exception.RegistrationWithBadEmail
import com.example.shared.exception.RegistrationWithBadPassword
import kotlinx.coroutines.launch
import timber.log.Timber

class RegistrationViewModel constructor(private val signUpInteractor: SignUpInteractor) :
    ViewModel() {

    sealed class RegistrationState {
        class RegistrationCompleted(val basicUserInfoEntity: BasicUserInfoEntity) :
            RegistrationState()

        class RegistrationWithInvalidFields(val fields: List<Pair<String, String>>) :
            RegistrationState()

        object RegistrationWithBadEmail : RegistrationState()
        object RegistrationWithBadPassword : RegistrationState()
    }

    companion object {
        val INPUT_USERNAME = "INPUT_USERNAME" to "empty username"
        val INPUT_PASSWORD = "INPUT_PASSWORD" to "empty password"
    }

    private val _registrationState = MutableLiveData<ViewState<RegistrationState>>()
    val registrationState: LiveData<ViewState<RegistrationState>>
        get() = _registrationState

    /**
     * Create new user
     */
    fun registrateUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                if (validateFields(email, password)) {
                    _registrationState.value = ViewState(ViewState.Status.LOADING)

                    val loggedUser = signUpInteractor.execute(
                        LoginParameterEntity(
                            email = email,
                            password = password
                        )
                    )

                    _registrationState.value =
                        ViewState(
                            ViewState.Status.SUCCESS,
                            RegistrationState.RegistrationCompleted(loggedUser)
                        )
                }
            } catch (error: Exception) {
                Timber.e(error)

                when (error) {
                    is RegistrationWithBadEmail -> _registrationState.value = ViewState(
                        ViewState.Status.SUCCESS,
                        RegistrationState.RegistrationWithBadEmail
                    )
                    is RegistrationWithBadPassword -> _registrationState.value = ViewState(
                        ViewState.Status.SUCCESS,
                        RegistrationState.RegistrationWithBadPassword
                    )
                    else -> _registrationState.value =
                        ViewState(ViewState.Status.ERROR, error = error)
                }
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
            _registrationState.value = ViewState(
                ViewState.Status.SUCCESS,
                data = RegistrationState.RegistrationWithInvalidFields(invalidFields)
            )
            return false
        }
        return true
    }
}
