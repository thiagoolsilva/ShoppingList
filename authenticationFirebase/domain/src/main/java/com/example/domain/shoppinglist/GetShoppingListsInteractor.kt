/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.domain.shoppinglist

import com.example.domain.models.BasicUserInfoEntity
import com.example.domain.models.ShoppingListEntity
import com.example.domain.repository.AuthenticationRepository
import com.example.domain.repository.ShoppingListRepository
import com.example.shared.exception.UserNotLogged

class GetShoppingListsInteractor constructor(
    private val shoppingListRepository: ShoppingListRepository<ShoppingListEntity>,
    private val authenticationRepository: AuthenticationRepository<BasicUserInfoEntity>
) {

    suspend fun execute(): List<ShoppingListEntity> {
        // get logged user
        val currentUser = authenticationRepository.currentUser()
        if (currentUser != null) {
            return shoppingListRepository.getShoppingLists(currentUser.id)
        } else {
            throw UserNotLogged("user not logged.")
        }
    }
}