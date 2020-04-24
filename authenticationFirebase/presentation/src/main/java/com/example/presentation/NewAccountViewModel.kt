/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.interactor.auth.SignUpInteractor
import com.example.domain.models.BasicUserInfoEntity
import com.example.domain.models.LoginParameterEntity
import com.example.presentation.model.ViewState
import kotlinx.coroutines.launch
import timber.log.Timber

class NewAccountViewModel constructor(private val signUpInteractor: SignUpInteractor) :
    ViewModel() {

    private val signUpState = MutableLiveData<ViewState<BasicUserInfoEntity>>()

    /**
     * Get sign up state
     */
    fun getSignUpState() = signUpState

    /**
     * Create new user
     */
    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            try {
                signUpState.postValue(ViewState(ViewState.Status.LOADING))

                val loggedUser =
                    signUpInteractor.execute(LoginParameterEntity(email = email, password = password))

                signUpState.postValue(ViewState(ViewState.Status.SUCCESS, loggedUser))
            } catch (error: Exception) {
                Timber.e(error)

                signUpState.postValue(ViewState(ViewState.Status.ERROR, error = error))
            }
        }
    }
}