/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.domain.shoppinglist

import com.example.domain.models.ShoppingListEntity
import com.example.domain.repository.ShoppingListRepository

class GetShoppingListsInteractor constructor(private val shoppingListRepository: ShoppingListRepository<ShoppingListEntity>) {

    suspend fun execute(): List<ShoppingListEntity> = shoppingListRepository.getShoppingLists()

}