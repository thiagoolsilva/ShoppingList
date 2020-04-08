/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.domain.repository

interface AuthenticationRepository<T> {

    fun authenticateUser(name:String, password:String): T?

    fun isLogged():Boolean

    fun signUp(name:String, password:String) :T?

    fun logout()

    fun currentUser() : T?

}