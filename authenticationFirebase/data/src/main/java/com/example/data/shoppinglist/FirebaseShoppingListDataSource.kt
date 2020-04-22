/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.data.shoppinglist

import com.example.domain.models.InputShoppingListEntity
import com.example.domain.models.InputShoppingListItemEntity
import com.example.domain.models.ShoppingListEntity
import com.example.domain.models.ShoppingListItemEntity
import com.example.domain.repository.ShoppingListRepository
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseShoppingListDataSource : ShoppingListRepository {

    private val db = Firebase.firestore

    companion object ShoppingList {
        const val SHOPPING_LIST_ITEM = "shoppingItem"
        const val SHOPPING_LIST_NODE = "shoppingList"
        const val FIELD_OWNER_FIELD = "owner"
        const val FIELD_SHOPPING_UUID = "uuid"
        const val FIELD_SHOPPING_LIST_ID = "shoppingListId"
        const val SHOPPING_ITEM_NODE = "items"
    }

    override suspend fun isShoppingListOwner(shoppingListId: String, owner: String): Boolean {
        return suspendCoroutine { continuation ->
            db.collection(SHOPPING_LIST_NODE)
                .whereEqualTo(FIELD_OWNER_FIELD, owner)
                .whereEqualTo(FIELD_SHOPPING_UUID, shoppingListId)
                .get()
                .addOnSuccessListener {
                    continuation.resume(true)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }


    override suspend fun getShoppingLists(owner: String): List<ShoppingListEntity> {
        return suspendCoroutine { continuation ->
            db.collection(SHOPPING_LIST_NODE)
                .whereEqualTo(FIELD_OWNER_FIELD, owner)
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

    override suspend fun saveShoppingList(inputShoppingListEntity: InputShoppingListEntity): String {
        return suspendCoroutine { continuation ->
            // set creation date using server timestamp
            inputShoppingListEntity.apply {
                creationDate = FieldValue.serverTimestamp()
            }

            // save object in firestone
            db.collection(SHOPPING_LIST_NODE)
                .add(inputShoppingListEntity)
                .addOnSuccessListener { result ->
                    continuation.resume(result.id)
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }


    override suspend fun getShoppingListItems(
        shoppingListId: String,
        owner: String
    ): List<ShoppingListItemEntity> {
        return suspendCoroutine { continuation ->
            db.collection(SHOPPING_LIST_ITEM)
                .whereEqualTo(FIELD_SHOPPING_LIST_ID, shoppingListId)
                .whereEqualTo(FIELD_OWNER_FIELD, owner)
                .get()
                .addOnSuccessListener { result ->
                    val shoppingItems = result.toObjects(ShoppingListItemEntity::class.java)
                    continuation.resume(shoppingItems)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override suspend fun saveShoppingListItem(inputShoppingListItemEntity: InputShoppingListItemEntity): String {
        return suspendCoroutine { continuation ->
            db.collection(SHOPPING_LIST_ITEM)
                .add(inputShoppingListItemEntity)
                .addOnSuccessListener {
                    continuation.resume(it.id)
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }
}