/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.domain.models

// in
data class InputShoppingListEntity constructor(
    var name: String,
    var owner: String,
    var creationDate: Any? = null,
    var uuid: String
)

data class InputShoppingListItemEntity constructor(
    var description: String = "",
    var check: Boolean = false,
    var uuid:String,
    var owner: String,
    var shoppingListId:String
)

// out
data class ShoppingListEntity constructor(
    val name: String = "",
    val owner: String = "",
    val uuid: String = ""
)

data class ShoppingListItemEntity constructor(
    var description: String = "",
    var check: Boolean = false,
    var uuid:String = ""
)
