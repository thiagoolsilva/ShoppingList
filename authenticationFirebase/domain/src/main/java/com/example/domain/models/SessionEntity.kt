/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.domain.models

// in
data class LoginParameterEntity constructor(val email: String, val password: String)

// out
data class BasicUserInfoEntity constructor(val email:String, val name:String?)
