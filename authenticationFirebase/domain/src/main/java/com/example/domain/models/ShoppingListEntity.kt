/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.domain.models

// in
data class BasicShoppingListEntity constructor(
    var name: String,
    var owner: String,
    var creationDate: Any? = null,
    var uuid:String
)

// out
data class ShoppingListEntity constructor(
    val name: String = "",
    val owner: String = "",
    val uuid: String = ""
) {}
