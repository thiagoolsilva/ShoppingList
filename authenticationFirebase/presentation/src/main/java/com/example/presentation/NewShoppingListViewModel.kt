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
import com.example.domain.shoppinglist.SaveShoppingListNameInteractor
import com.example.presentation.model.ViewState
import com.example.shared.exception.UserNotLogged
import kotlinx.coroutines.launch
import timber.log.Timber

class NewShoppingListViewModel constructor(private val saveShoppingListNameInteractor: SaveShoppingListNameInteractor) :
    ViewModel() {

    private val _shoppingListState = MutableLiveData<ViewState<String>>()
    val shoppingListState: LiveData<ViewState<String>>
        get() = _shoppingListState

    /**
     * Save provided Shopping list name
     */
    fun saveShoppingListName(name: String) {
        viewModelScope.launch {
            try {
                _shoppingListState.postValue(
                    ViewState(ViewState.Status.LOADING)
                )
                val documentID = saveShoppingListNameInteractor.saveShoppingListName(name)

                _shoppingListState.postValue(ViewState(ViewState.Status.SUCCESS, documentID))
            } catch (error: Exception) {
                Timber.e(error)

                _shoppingListState.postValue(ViewState(ViewState.Status.ERROR, error = error))
            }
        }
    }

}