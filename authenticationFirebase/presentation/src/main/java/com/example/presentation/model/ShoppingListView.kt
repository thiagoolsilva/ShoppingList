/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.presentation.model

data class ShoppingListView constructor(val name:String, val shoppingListId:String) {}

data class ShoppingListItemView constructor(val description:String, val shoppingId:String, var check:Boolean = false, var uuid: String = "") {}