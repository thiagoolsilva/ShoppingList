/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.data.shoppinglist

import com.example.domain.models.InputShoppingListEntity
import com.example.domain.models.InputShoppingListItemEntity
import com.example.domain.models.ShoppingListEntity
import com.example.domain.models.ShoppingListItemEntity
import com.example.domain.repository.ShoppingListRepository
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FirebaseShoppingListDataSource : ShoppingListRepository {

    private val db = Firebase.firestore

    companion object {
        const val SHOPPING_LIST_NODE = "shoppingList"
        const val SHOPPING_LIST_OWNER_FIELD = "owner"
        const val SHOPPING_LIST_UUID_FIELD = "uuid"

        const val SHOPPING_LIST_ITEM = "shoppingItem"
        const val FIELD_SHOPPING_LIST_ID_FIELD = "shoppingListId"
        const val FIELD_SHOPPING_ITEM_DESCRIPTION_FIELD = "description"
        const val FIELD_SHOPPING_ITEM_CHECK_FIELD = "check"
        const val FIELD_SHOPPING_ITEM_OWNER_FIELD = "owner"
        const val FIELD_SHOPPING_ITEM_UUID_FIELD = "uuid"
    }

    override suspend fun isShoppingListOwner(shoppingListId: String, owner: String): Boolean {
        return !(db.collection(SHOPPING_LIST_NODE)
            .whereEqualTo(SHOPPING_LIST_OWNER_FIELD, owner)
            .whereEqualTo(SHOPPING_LIST_UUID_FIELD, shoppingListId)
            .get()
            .await()
            .isEmpty)
    }

    override suspend fun getShoppingLists(owner: String): List<ShoppingListEntity> {
        return db.collection(SHOPPING_LIST_NODE)
            .whereEqualTo(SHOPPING_LIST_OWNER_FIELD, owner)
            .get()
            .await()
            .toObjects(ShoppingListEntity::class.java)
    }

    override suspend fun saveShoppingList(item: InputShoppingListEntity): String {
        // set creation date using server timestamp
        item.apply {
            creationDate = FieldValue.serverTimestamp()
        }

        // save object in firestone
        return db.collection(SHOPPING_LIST_NODE)
            .add(item)
            .await()
            .id
    }

    override suspend fun getShoppingListItems(
        shoppingListId: String,
        owner: String
    ): List<ShoppingListItemEntity> {
        return db.collection(SHOPPING_LIST_ITEM)
            .whereEqualTo(FIELD_SHOPPING_LIST_ID_FIELD, shoppingListId)
            .whereEqualTo(SHOPPING_LIST_OWNER_FIELD, owner)
            .get()
            .await()
            .toObjects(ShoppingListItemEntity::class.java)
    }

    override suspend fun saveShoppingListItem(inputShoppingListItemEntity: InputShoppingListItemEntity): String {
        return db.collection(SHOPPING_LIST_ITEM)
            .add(inputShoppingListItemEntity)
            .await()
            .id
    }

    override suspend fun updateShoppingListItem(item: InputShoppingListItemEntity) {
        // get document id to be updated
        val document = db.collection(SHOPPING_LIST_ITEM)
            .whereEqualTo(FIELD_SHOPPING_ITEM_UUID_FIELD, item.uuid)
            .whereEqualTo(FIELD_SHOPPING_ITEM_OWNER_FIELD, item.owner)
            .get()
            .await()
            .documents.map {
                it.id
            }

        if (document.isNotEmpty()) {
            val firstDocumentId = document.first()

            // update document with provided item information
            val updatedFields = hashMapOf<String, Any>(
                FIELD_SHOPPING_ITEM_DESCRIPTION_FIELD to item.description,
                FIELD_SHOPPING_ITEM_CHECK_FIELD to item.check
            )

            // call update firestone function
            db.collection(SHOPPING_LIST_ITEM)
                .document(firstDocumentId)
                .update(updatedFields)
                .await()
        }
    }
}
