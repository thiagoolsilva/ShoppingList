/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.data.shoppinglist

import com.example.domain.models.ShoppingListEntity
import com.example.domain.repository.ShoppingListRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseShoppingListDataSource : ShoppingListRepository<ShoppingListEntity> {

    private val db = Firebase.firestore

    companion object {
        private const val SHOPPING_LIST_NODE = "shoppingList"
    }

    override suspend fun getShoppingLists(): List<ShoppingListEntity> {
        return suspendCoroutine { continuation ->
            db.collection(SHOPPING_LIST_NODE)
                .get()
                .addOnSuccessListener { result ->
                    val shoppingListNames = result.toObjects(ShoppingListEntity::class.java)
                    continuation.resume(shoppingListNames)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }

}