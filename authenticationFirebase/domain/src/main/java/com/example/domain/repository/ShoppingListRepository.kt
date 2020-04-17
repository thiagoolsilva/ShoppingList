/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.domain.repository

interface ShoppingListRepository<T> {

    suspend fun getShoppingLists(): List<T>

}