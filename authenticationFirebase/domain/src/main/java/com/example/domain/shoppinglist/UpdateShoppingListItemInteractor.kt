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
import com.example.shared.exception.UserNotLogged
import java.lang.IllegalArgumentException

class UpdateShoppingListItemInteractor constructor(
    private val shoppingListRepository: ShoppingListRepository,
    private val authenticationRepository: AuthenticationRepository<BasicUserInfoEntity>
) {

    suspend fun updateShoppingItem(
        description: String,
        checkStatus: Boolean?,
        uuid: String,
        shoppingListId: String
    ) {
        if (uuid.isEmpty() || description.isEmpty()) {
            throw IllegalArgumentException("The description and uuid must be not null.")
        }

        val currentUser = authenticationRepository.currentUser()
        if (currentUser != null) {
            val shoopingListItem = InputShoppingListItemEntity(
                description = description,
                check = checkStatus ?: false,
                uuid = uuid,
                shoppingListId = shoppingListId,
                owner = currentUser.id
            )

            shoppingListRepository.updateShoppingListItem(shoopingListItem)
        } else {
            throw UserNotLogged("user not logged.")
        }
    }
}