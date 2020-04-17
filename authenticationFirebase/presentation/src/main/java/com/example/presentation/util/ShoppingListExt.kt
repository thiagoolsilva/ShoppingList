/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.presentation.util

import com.example.domain.models.ShoppingListEntity
import com.example.presentation.model.ShoppingListView


fun ShoppingListEntity.toShoppingListView(): ShoppingListView =
    ShoppingListView(name = this.name, shoppingListId = this.owner)
