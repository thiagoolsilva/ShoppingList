/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.domain.shoppinglist

import com.example.domain.models.BasicShoppingListEntity
import com.example.domain.models.ShoppingListEntity
import com.example.domain.repository.ShoppingListRepository

class SaveShoppingListNameInteractor constructor(private val shoppingListRepository: ShoppingListRepository<ShoppingListEntity>) {

    /**
     * Save provided shopping list information
     */
    suspend fun saveShoppingListName(basicShoppingListEntity: BasicShoppingListEntity): String {
        return shoppingListRepository.saveShoppingList(basicShoppingListEntity)
    }
}