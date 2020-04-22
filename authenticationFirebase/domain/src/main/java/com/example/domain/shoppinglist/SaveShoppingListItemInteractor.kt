/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.domain.shoppinglist

import com.example.domain.models.BasicUserInfoEntity
import com.example.domain.models.InputShoppingListItemEntity
import com.example.domain.repository.AuthenticationRepository
import com.example.domain.repository.ShoppingListRepository
import com.example.shared.exception.NotShoppingListOwner
import com.example.shared.exception.UserNotLogged
import java.util.*

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
                        uuid = UUID.randomUUID().toString()
                    )
                )
            } else {
                throw NotShoppingListOwner("current user is not owner of provided shopping id.")
            }
        } else {
            throw UserNotLogged("user not logged.")
        }
    }

}