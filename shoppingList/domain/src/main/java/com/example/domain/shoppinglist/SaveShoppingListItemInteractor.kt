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

package com.example.domain.shoppinglist

import com.example.domain.models.BasicUserInfoEntity
import com.example.domain.models.InputShoppingListItemEntity
import com.example.domain.repository.AuthenticationRepository
import com.example.domain.repository.ShoppingListRepository
import com.example.shared.exception.ShoppingOnwerError
import com.example.shared.exception.UserNotLogged
import java.util.UUID

class SaveShoppingListItemInteractor constructor(
    private val shoppingListRepository: ShoppingListRepository,
    private val authenticationRepository: AuthenticationRepository<BasicUserInfoEntity>
) {

    suspend fun execute(itemDescription: String, shoppingListId: String): String {
        val currentUser = authenticationRepository.currentUser()
        if (currentUser != null) {
            val isShoppingListOwner = shoppingListRepository.isShoppingListOwner(
                shoppingListId = shoppingListId,
                owner = currentUser.id
            )

            // check if provided shopping list id belongs to user
            if (isShoppingListOwner) {
                return shoppingListRepository.saveShoppingListItem(
                    InputShoppingListItemEntity(
                        description = itemDescription,
                        uuid = UUID.randomUUID().toString(),
                        owner = currentUser.id,
                        shoppingListId = shoppingListId
                    )
                )
            } else {
                throw ShoppingOnwerError("current user is not owner of provided shopping id.")
            }
        } else {
            throw UserNotLogged("user not logged.")
        }
    }
}
