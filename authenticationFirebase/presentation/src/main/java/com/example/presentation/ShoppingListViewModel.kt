/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.presentation

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

    val shoppingListState = MutableLiveData<ViewState<List<ShoppingListView>>>()

    fun fetchShoppingList() {
        viewModelScope.launch {
            try {
                shoppingListState.postValue(ViewState(ViewState.Status.LOADING))

                val shoppingList =
                    getShoppingListsInteractor.execute().map { it.toShoppingListView() }

                shoppingListState.postValue(ViewState(ViewState.Status.SUCCESS, shoppingList))
            } catch (error: Exception) {
                Timber.e(error)

                shoppingListState.postValue(ViewState(ViewState.Status.ERROR, error = error))
            }
        }
    }

}