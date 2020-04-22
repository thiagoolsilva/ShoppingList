/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.presentation

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

    val shoppingListState = MutableLiveData<ViewState<String>>()

    /**
     * Save provided Shopping list name
     */
    fun saveShoppingListName(name: String) {
        viewModelScope.launch {
            try {
                val documentID = saveShoppingListNameInteractor.saveShoppingListName(name)

                shoppingListState.postValue(ViewState(ViewState.Status.SUCCESS, documentID))
            } catch (error: UserNotLogged) {
                shoppingListState.postValue(ViewState(ViewState.Status.ERROR, error = error))
            } catch (error: Exception) {
                shoppingListState.postValue(ViewState(ViewState.Status.ERROR, error = error))
            }
        }
    }

}