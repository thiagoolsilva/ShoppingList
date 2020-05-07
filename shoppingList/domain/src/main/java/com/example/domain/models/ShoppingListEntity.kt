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
    var uuid: String,
    var owner: String,
    var shoppingListId: String
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
    var uuid: String = ""
)
