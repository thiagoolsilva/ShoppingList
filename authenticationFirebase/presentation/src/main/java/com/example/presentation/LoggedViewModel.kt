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
import com.example.domain.interactor.auth.LogoutInteractor
import com.example.presentation.model.UserInfoView
import com.example.presentation.model.ViewState
import com.example.presentation.util.toUserInfoView
import kotlinx.coroutines.launch
import timber.log.Timber

class LoggedViewModel constructor(
    private val getLoggedUserInteractor: GetLoggedUserInteractor,
    private val logoutInteractor: LogoutInteractor
) :
    ViewModel() {

    private val currentUserState = MutableLiveData<ViewState<UserInfoView?>>()
    private val logoutState = MutableLiveData<ViewState<Unit>>()

    /**
     * Get user State
     */
    fun getUserState() = currentUserState

    /**
     * Get logout State
     */
    fun getLogoutState() = logoutState


    /**
     * Fetch user state
     */
    fun fetchUserState() {
        viewModelScope.launch {
            try {
                currentUserState.postValue(ViewState(ViewState.Status.LOADING))

                val userState = getLoggedUserInteractor.execute()?.toUserInfoView()
                currentUserState.postValue(ViewState(ViewState.Status.SUCCESS, userState))
            } catch (error: Exception) {
                currentUserState.postValue(ViewState(ViewState.Status.ERROR, error = error))
            }
        }
    }

    /**
     * logout current user
     */
    fun logout() {
        viewModelScope.launch {
            try {
                logoutState.postValue(ViewState(ViewState.Status.LOADING))

                logoutInteractor.execute()

                logoutState.postValue(ViewState(ViewState.Status.SUCCESS))
            } catch (error:Exception) {
                Timber.e(error)

                logoutState.postValue(ViewState(ViewState.Status.ERROR, error = error))
            }
        }
    }
}