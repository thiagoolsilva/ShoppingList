/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.presentation.util

import com.example.domain.models.BasicUserInfoEntity
import com.example.presentation.model.UserInfoView

fun BasicUserInfoEntity.toUserInfoView(): UserInfoView =
    UserInfoView(name = this.name ?: "", email = this.email)
