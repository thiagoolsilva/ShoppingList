/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.domain.shoppinglist

import com.example.domain.models.BasicUserInfoEntity
import com.example.domain.models.ShoppingListItemEntity
import com.example.domain.repository.AuthenticationRepository
import com.example.domain.repository.ShoppingListRepository
import com.example.shared.exception.UserNotLogged

class GetShoppingListItemsInteractor constructor(
    private val shoppingListRepository: ShoppingListRepository,
    private val authenticationRepository: AuthenticationRepository<BasicUserInfoEntity>
) {

    suspend fun execute(shoppingListId: String): List<ShoppingListItemEntity> {
        val currentUser = authenticationRepository.currentUser()
        if (currentUser != null) {
            return shoppingListRepository.getShoppingListItems(
                shoppingListId = shoppingListId,
                owner = currentUser.id
            )
        } else {
            throw UserNotLogged("user not logged.")
        }
    }
}