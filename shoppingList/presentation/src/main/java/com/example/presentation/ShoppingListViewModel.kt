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