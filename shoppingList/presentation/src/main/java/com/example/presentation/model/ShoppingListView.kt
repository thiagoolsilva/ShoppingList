/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.presentation.model

data class ShoppingListView constructor(val name: String, val shoppingListId: String)

data class ShoppingListItemView constructor(
    var description: String,
    var check: Boolean = false,
    var uuid: String = ""
)