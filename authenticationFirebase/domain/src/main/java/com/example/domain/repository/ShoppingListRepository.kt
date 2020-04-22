/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.domain.repository

import com.example.domain.models.BasicShoppingListEntity

interface ShoppingListRepository<T> {

    suspend fun getShoppingLists(): List<T>

    suspend fun saveShoppingList(BasicShoppingListEntity: BasicShoppingListEntity) : String

}