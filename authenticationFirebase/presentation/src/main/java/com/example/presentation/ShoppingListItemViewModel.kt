/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.shoppinglist.GetShoppingListItemsInteractor
import com.example.domain.shoppinglist.SaveShoppingListItemInteractor
import com.example.presentation.model.ShoppingListItemView
import com.example.presentation.model.ViewState
import com.example.presentation.util.toShoppingListItemView
import kotlinx.coroutines.launch
import java.lang.Exception

class ShoppingListItemViewModel constructor(
    private val getShoppingListItemsInteractor: GetShoppingListItemsInteractor,
    private val saveShoppingListItemInteractor: SaveShoppingListItemInteractor
) :
    ViewModel() {

    val shoppingListItemsState = MutableLiveData<ViewState<List<ShoppingListItemView>>>()
    val saveShoppingListItemState = MutableLiveData<ViewState<String>>()

    /**
     * Save shopping list item
     */
    fun saveShoppingListItem(itemDescription: String, shoppingListId: String) {
        viewModelScope.launch {
            try {
                saveShoppingListItemState.postValue(ViewState(ViewState.Status.LOADING))
                val resultSet = saveShoppingListItemInteractor.execute(
                    itemDescription = itemDescription,
                    shoppingListId = shoppingListId
                )
                saveShoppingListItemState.postValue(
                    ViewState(
                        ViewState.Status.SUCCESS,
                        data = resultSet
                    )
                )
            } catch (error: Exception) {
                saveShoppingListItemState.postValue(
                    ViewState(
                        ViewState.Status.ERROR,
                        error = error
                    )
                )
            }
        }
    }

    /**
     * Gets shopping list items from provided shopping item id
     */
    fun getShoppingItems(shoppingListId: String) {
        viewModelScope.launch {
            try {
                shoppingListItemsState.postValue(ViewState(ViewState.Status.LOADING))

                val shoppingListItems =
                    getShoppingListItemsInteractor.execute(shoppingListId = shoppingListId).map {
                        it.toShoppingListItemView()
                    }

                shoppingListItemsState.postValue(
                    ViewState(
                        ViewState.Status.SUCCESS,
                        shoppingListItems
                    )
                )
            } catch (error: Exception) {
                shoppingListItemsState.postValue(ViewState(ViewState.Status.ERROR, error = error))
            }
        }
    }
}