/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.data.shoppinglist

import com.example.domain.models.BasicShoppingListEntity
import com.example.domain.models.ShoppingListEntity
import com.example.domain.repository.ShoppingListRepository
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseShoppingListDataSource : ShoppingListRepository<ShoppingListEntity> {

    private val db = Firebase.firestore

    companion object ShoppingList {

        const val SHOPPING_LIST_NODE = "shoppingList"
        const val FIELD_FIELD_OWNER_FIELD = "owner"

    }


    override suspend fun getShoppingLists(owner: String): List<ShoppingListEntity> {
        return suspendCoroutine { continuation ->
            db.collection(SHOPPING_LIST_NODE)
                .whereEqualTo(FIELD_FIELD_OWNER_FIELD, owner)
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

    override suspend fun saveShoppingList(basicShoppingListEntity: BasicShoppingListEntity): String {
        return suspendCoroutine { continuation ->
            // set creation date using server timestamp
            basicShoppingListEntity.apply {
                creationDate = FieldValue.serverTimestamp()
            }

            // save object in firestone
            db.collection(SHOPPING_LIST_NODE)
                .add(basicShoppingListEntity)
                .addOnSuccessListener { result ->
                    continuation.resume(result.id)
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }


}