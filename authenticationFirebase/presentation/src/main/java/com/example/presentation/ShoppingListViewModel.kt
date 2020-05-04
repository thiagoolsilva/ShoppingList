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
import com.example.domain.shoppinglist.GetShoppingListsInteractor
import com.example.presentation.model.ShoppingListView
import com.example.presentation.model.ViewState
import com.example.presentation.util.toShoppingListView
import kotlinx.coroutines.launch
import timber.log.Timber

class ShoppingListViewModel constructor(private val getShoppingListsInteractor: GetShoppingListsInteractor) :
    ViewModel() {

    private val _shoppingListState = MutableLiveData<ViewState<List<ShoppingListView>>>()
    val shoppingListState: LiveData<ViewState<List<ShoppingListView>>>
        get() = _shoppingListState

    fun fetchShoppingList() {
        viewModelScope.launch {
            try {
                _shoppingListState.value = ViewState(ViewState.Status.LOADING)

                val shoppingList =
                    getShoppingListsInteractor.execute().map { it.toShoppingListView() }

                _shoppingListState.value = ViewState(ViewState.Status.SUCCESS, shoppingList)
            } catch (error: Exception) {
                Timber.e(error)

                _shoppingListState.value = ViewState(ViewState.Status.ERROR, error = error)
            }
        }
    }

}