/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.domain.repository

import com.example.domain.models.InputShoppingListEntity
import com.example.domain.models.InputShoppingListItemEntity
import com.example.domain.models.ShoppingListEntity
import com.example.domain.models.ShoppingListItemEntity

interface ShoppingListRepository {

    suspend fun isShoppingListOwner(shoppingListId: String, owner: String) : Boolean

    suspend fun getShoppingLists(owner: String): List<ShoppingListEntity>

    suspend fun saveShoppingList(InputShoppingListEntity: InputShoppingListEntity): String

    suspend fun getShoppingListItems(
        shoppingListId: String,
        owner: String
    ): List<ShoppingListItemEntity>

    suspend fun saveShoppingListItem(inputShoppingListItemEntity: InputShoppingListItemEntity): String

    suspend fun updateShoppingListItem(inputShoppingListItemEntity: InputShoppingListItemEntity)

}