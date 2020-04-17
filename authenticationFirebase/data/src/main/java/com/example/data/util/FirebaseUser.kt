/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.data.util

import com.example.domain.models.BasicUserInfoEntity
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toBasicUserInfo(): BasicUserInfoEntity =
    BasicUserInfoEntity(name = this.displayName ?: "", email = this.email ?: "")
