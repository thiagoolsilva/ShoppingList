/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.domain.repository

interface AuthenticationRepository<T> {

    suspend fun isLogged(): Boolean

    suspend fun currentUser(): T?

    suspend fun authenticateUser(email: String, password: String):T

    suspend fun signUp(email: String, password: String): T

    suspend fun logout()

}
