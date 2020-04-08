/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.domain.models

// in
data class LoginParameter constructor(val email: String, val password: String)

// out
data class BasicUserInfo constructor(val email:String, val name:String?)
