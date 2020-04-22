/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.presentation.util

import com.example.domain.models.ShoppingListEntity
import com.example.domain.models.ShoppingListItemEntity
import com.example.presentation.model.ShoppingListItemView
import com.example.presentation.model.ShoppingListView


fun ShoppingListEntity.toShoppingListView(): ShoppingListView =
    ShoppingListView(name = this.name, shoppingListId = this.uuid)

fun ShoppingListItemEntity.toShoppingListItemView() : ShoppingListItemView =
    ShoppingListItemView(description = this.item, shoppingId = this.shoppingListId, check = this.check)
