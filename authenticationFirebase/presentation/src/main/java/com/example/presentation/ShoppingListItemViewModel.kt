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

    private val _shoppingListItemsState = MutableLiveData<ViewState<List<ShoppingListItemView>>>()
    val shoppingListItemsState: LiveData<ViewState<List<ShoppingListItemView>>>
        get() = _shoppingListItemsState

    private val _saveShoppingListItemState = MutableLiveData<ViewState<String>>()
    val saveShoppingListItemState: LiveData<ViewState<String>>
        get() = _saveShoppingListItemState

    private val _updateShoppingListItemState = MutableLiveData<ViewState<Number>>()
    val updateShoppingListItemState: LiveData<ViewState<Number>>
        get() = _updateShoppingListItemState


    /**
     * Update shopping list item
     */
    fun updateShoppingListItem(
        ItemDescription: String,
        shoppingListId: String,
        checkStatus: Boolean,
        uuid: String
    ) {
        viewModelScope.launch {
            try {
                _updateShoppingListItemState.postValue(ViewState(ViewState.Status.LOADING))

                updateShoppingListItemInteractor.updateShoppingItem(
                    description = ItemDescription,
                    checkStatus = checkStatus,
                    shoppingListId = shoppingListId,
                    uuid = uuid
                )

                _updateShoppingListItemState.postValue(
                    ViewState(
                        ViewState.Status.SUCCESS
                    )
                )
            } catch (error: Exception) {
                Timber.e(error)

                _updateShoppingListItemState.postValue(
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
                _saveShoppingListItemState.postValue(ViewState(ViewState.Status.LOADING))
                val resultSet = saveShoppingListItemInteractor.execute(
                    itemDescription = itemDescription,
                    shoppingListId = shoppingListId
                )
                _saveShoppingListItemState.postValue(
                    ViewState(
                        ViewState.Status.SUCCESS,
                        data = resultSet
                    )
                )
            } catch (error: Exception) {
                _saveShoppingListItemState.postValue(
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
                _shoppingListItemsState.postValue(ViewState(ViewState.Status.LOADING))

                val shoppingListItems =
                    getShoppingListItemsInteractor.execute(shoppingListId = shoppingListId).map {
                        it.toShoppingListItemView()
                    }

                _shoppingListItemsState.postValue(
                    ViewState(
                        ViewState.Status.SUCCESS,
                        shoppingListItems
                    )
                )
            } catch (error: Exception) {
                Timber.e(error)
                _shoppingListItemsState.postValue(ViewState(ViewState.Status.ERROR, error = error))
            }
        }
    }
}