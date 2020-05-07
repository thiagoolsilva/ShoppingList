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

package com.example.domain.repository

interface AuthenticationRepository<T> {

    suspend fun isLogged(): Boolean

    suspend fun currentUser(): T?

    suspend fun updateProfile(name: String)

    suspend fun authenticateUser(email: String, password: String):T

    suspend fun signUp(email: String, password: String): T

    suspend fun logout()

}
