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
import com.example.domain.interactor.auth.GetLoggedUserInteractor
import com.example.presentation.model.UserInfoView
import com.example.presentation.model.ViewState
import com.example.presentation.util.toUserInfoView
import kotlinx.coroutines.launch

class ProfileViewModel constructor(
    private val getLoggedUserInteractor: GetLoggedUserInteractor
) :
    ViewModel() {

    private val _currentUserState = MutableLiveData<ViewState<UserInfoView?>>()
    val currentUserState: LiveData<ViewState<UserInfoView?>>
        get() = _currentUserState

    /**
     * Fetch user state
     */
    fun fetchUserState() {
        viewModelScope.launch {
            try {
                _currentUserState.postValue(ViewState(ViewState.Status.LOADING))

                val userState = getLoggedUserInteractor.execute()?.toUserInfoView()
                _currentUserState.postValue(ViewState(ViewState.Status.SUCCESS, userState))
            } catch (error: Exception) {
                _currentUserState.postValue(ViewState(ViewState.Status.ERROR, error = error))
            }
        }
    }

}