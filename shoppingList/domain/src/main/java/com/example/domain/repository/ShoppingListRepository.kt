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