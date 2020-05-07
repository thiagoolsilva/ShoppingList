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
import com.example.domain.interactor.auth.GetLoggedUserInteractor
import com.example.domain.interactor.auth.UpdateProfileInfoInteractor
import com.example.presentation.model.UserInfoView
import com.example.presentation.model.ViewState
import com.example.presentation.util.toUserInfoView
import kotlinx.coroutines.launch

class ProfileViewModel constructor(
    private val getLoggedUserInteractor: GetLoggedUserInteractor,
    private val updateProfileInfoInteractor: UpdateProfileInfoInteractor
) :
    ViewModel() {

    sealed class UserPofileState {
        class CollectedProfileState(val userInfoView: UserInfoView?) : UserPofileState()
        object UpdatedProfileState : UserPofileState()
        class InvalidProfileFieldsState(val fields: List<Pair<String, String>>) : UserPofileState()
    }

    companion object {
        val INPUT_USERNAME = "INPUT_USERNAME" to "empty username"
    }

    private val _currentUserState = MutableLiveData<ViewState<UserPofileState>>()
    val currentUserState: LiveData<ViewState<UserPofileState>>
        get() = _currentUserState

    /**
     * Fetch user state
     */
    fun fetchUserProfile() {
        viewModelScope.launch {
            try {
                // send loading event to live data
                _currentUserState.value = ViewState(ViewState.Status.LOADING)

                // get user profile from repository
                val userState = getLoggedUserInteractor.execute()?.toUserInfoView()

                // send user profile to LiveData
                _currentUserState.value = ViewState(
                    ViewState.Status.SUCCESS,
                    UserPofileState.CollectedProfileState(userState)
                )
            } catch (error: Exception) {
                _currentUserState.value = ViewState(ViewState.Status.ERROR, error = error)
            }
        }
    }

    /**
     * Update user profile
     * @param name display name
     */
    fun updateUserProfile(name: String) {
        viewModelScope.launch {
            try {
                if (validateFields(name)) {
                    // send loading event to live data
                    _currentUserState.value = ViewState(ViewState.Status.LOADING)

                    updateProfileInfoInteractor.execute(name)

                    // send user profile to LiveData
                    _currentUserState.value = ViewState(
                        ViewState.Status.SUCCESS,
                        UserPofileState.UpdatedProfileState
                    )
                }
            } catch (error: Exception) {
                _currentUserState.value = ViewState(ViewState.Status.ERROR, error = error)
            }
        }
    }

    /**
     * Validate required fields
     */
    private fun validateFields(login: String): Boolean {
        val invalidFields = arrayListOf<Pair<String, String>>()

        if (login.isEmpty()) {
            invalidFields.add(INPUT_USERNAME)
        }

        if (invalidFields.isNotEmpty()) {
            _currentUserState.value = ViewState(
                ViewState.Status.SUCCESS,
                data = UserPofileState.InvalidProfileFieldsState(invalidFields)
            )
            return false
        }
        return true
    }

}