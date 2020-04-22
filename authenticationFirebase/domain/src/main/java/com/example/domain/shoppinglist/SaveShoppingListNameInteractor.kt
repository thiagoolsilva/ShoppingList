/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.domain.shoppinglist

import com.example.shared.exception.UserNotLogged
import com.example.domain.models.BasicShoppingListEntity
import com.example.domain.models.BasicUserInfoEntity
import com.example.domain.models.ShoppingListEntity
import com.example.domain.repository.AuthenticationRepository
import com.example.domain.repository.ShoppingListRepository
import java.util.*

class SaveShoppingListNameInteractor constructor(
    private val shoppingListRepository: ShoppingListRepository<ShoppingListEntity>,
    private val authenticationRepository: AuthenticationRepository<BasicUserInfoEntity>
) {

    /**
     * Save provided shopping list information
     */
    suspend fun saveShoppingListName(shoppingListName: String): String {
        // get logged user
        val currentUser = authenticationRepository.currentUser()
        if (currentUser != null) {

            val shoppingListId = UUID.randomUUID().toString()
            val basicShoppingListEntity =
                BasicShoppingListEntity(name = shoppingListName, owner = currentUser.id, uuid = shoppingListId)

            return shoppingListRepository.saveShoppingList(basicShoppingListEntity)
        } else {
            throw UserNotLogged("user not logged.")
        }
    }
}