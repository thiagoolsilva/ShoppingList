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
import com.example.domain.shoppinglist.UpdateShoppingListItemInteractor
import com.example.presentation.model.ShoppingListItemView
import com.example.presentation.model.ViewState
import com.example.presentation.util.toShoppingListItemView
import kotlinx.coroutines.launch
import timber.log.Timber

class ShoppingListItemViewModel constructor(
    private val getShoppingListItemsInteractor: GetShoppingListItemsInteractor,
    private val saveShoppingListItemInteractor: SaveShoppingListItemInteractor,
    private val updateShoppingListItemInteractor: UpdateShoppingListItemInteractor
) :
    ViewModel() {

    val shoppingListItemsState = MutableLiveData<ViewState<List<ShoppingListItemView>>>()
    val saveShoppingListItemState = MutableLiveData<ViewState<String>>()
    val updateShoppingListItemState = MutableLiveData<ViewState<Number>>()

    fun updateShoppingListItem(
        ItemDescription: String,
        shoppingListId: String,
        checkStatus: Boolean,
        uuid: String
    ) {
        viewModelScope.launch {
            try {
                updateShoppingListItemState.postValue(ViewState(ViewState.Status.LOADING))

                updateShoppingListItemInteractor.updateShoppingItem(
                    description = ItemDescription,
                    checkStatus = checkStatus,
                    shoppingListId = shoppingListId,
                    uuid = uuid
                )

                updateShoppingListItemState.postValue(
                    ViewState(
                        ViewState.Status.SUCCESS
                    )
                )
            } catch (error: Exception) {
                Timber.e(error)

                updateShoppingListItemState.postValue(
                    ViewState(
                        ViewState.Status.ERROR,
                        error = error
                    )
                )
            }
        }
    }

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
                Timber.d("shopping list id = %s", shoppingListId)
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
                Timber.e(error)
                shoppingListItemsState.postValue(ViewState(ViewState.Status.ERROR, error = error))
            }
        }
    }
}