/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.data.model

import java.lang.Exception

// in
data class LoginParameter constructor(val email: String, val password: String)

// out
data class SessionInfo constructor(val userInfo: UserInfo?, val error: Exception)

data class UserInfo constructor(val name: String?, val email: String)
