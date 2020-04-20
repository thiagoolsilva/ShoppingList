/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.BasicShoppingListEntity
import com.example.domain.shoppinglist.SaveShoppingListNameInteractor
import com.example.presentation.model.ViewState
import kotlinx.coroutines.launch
import java.lang.Exception

class NewShoppingListViewModel constructor(private val saveShoppingListNameInteractor: SaveShoppingListNameInteractor) :
    ViewModel() {

    val shoppingListState = MutableLiveData<ViewState<String>>()

    /**
     * Save provided Shopping list name
     */
    fun saveShoppingListName(name: String, owner: String) {
        viewModelScope.launch {
            try {
                val basicShoppingListEntity = BasicShoppingListEntity(name = name, owner = owner)
                val documentID =
                    saveShoppingListNameInteractor.saveShoppingListName(basicShoppingListEntity)

                shoppingListState.postValue(ViewState(ViewState.Status.SUCCESS, documentID))
            } catch (error: Exception) {
                shoppingListState.postValue(ViewState(ViewState.Status.ERROR))
            }
        }
    }

}