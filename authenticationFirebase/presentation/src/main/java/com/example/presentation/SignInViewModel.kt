/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.interactor.auth.SignInUserInteractor
import com.example.domain.models.LoginParameterEntity
import com.example.presentation.model.UserInfoView
import com.example.presentation.model.ViewState
import com.example.presentation.util.toUserInfoView
import kotlinx.coroutines.launch

class SignInViewModel constructor(private val signInUserInteractor: SignInUserInteractor) :
    ViewModel() {

    private val currentLiveState = MutableLiveData<ViewState<UserInfoView>>()

    /**
     * Return LiveDate state instance
     * @return liveDate UserInfoView instance
     */
    fun getState(): LiveData<ViewState<UserInfoView>> = currentLiveState

    /**
     * Gets authenticate user
     * @param email user's email
     * @param password user's password
     */
    fun authenticateUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                currentLiveState.postValue(ViewState(ViewState.Status.LOADING))

                val authenticatedUser =
                    signInUserInteractor.execute(LoginParameterEntity(email, password))?.toUserInfoView()
                currentLiveState.postValue(ViewState(ViewState.Status.SUCCESS, authenticatedUser))

            } catch (error: Exception) {
                currentLiveState.postValue(ViewState(ViewState.Status.ERROR, error = error))
            }
        }
    }

}