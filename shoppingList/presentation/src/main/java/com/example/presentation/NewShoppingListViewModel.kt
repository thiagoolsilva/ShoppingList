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
import com.example.domain.shoppinglist.SaveShoppingListNameInteractor
import com.example.presentation.model.ViewState
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
                _shoppingListState.value =
                    ViewState(ViewState.Status.LOADING)

                val documentID = saveShoppingListNameInteractor.saveShoppingListName(name)

                _shoppingListState.value = ViewState(ViewState.Status.SUCCESS, documentID)
            } catch (error: Exception) {
                Timber.e(error)

                _shoppingListState.value = ViewState(ViewState.Status.ERROR, error = error)
            }
        }
    }
}
