/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.interactor.auth.GetLoggedUserInteractor
import com.example.domain.models.BasicUserInfo
import com.example.presentation.model.UserInfoView
import com.example.presentation.model.ViewState
import com.example.presentation.util.toUserInfoView
import kotlinx.coroutines.launch
import java.lang.Exception

class SplashViewModel constructor(private val getLoggedUserInteractor: GetLoggedUserInteractor): ViewModel() {

    private val currentUserState = MutableLiveData<ViewState<UserInfoView?>>()

    fun getUserState() = currentUserState

    fun fetchUserState() {
        viewModelScope.launch {
            try {
                currentUserState.postValue(ViewState(ViewState.Status.LOADING))

                val userState = getLoggedUserInteractor.execute()?.toUserInfoView()
                currentUserState.postValue(ViewState(ViewState.Status.SUCCESS, userState))
            } catch (error:Exception) {
                currentUserState.postValue(ViewState(ViewState.Status.ERROR, error = error))
            }
        }
    }
}