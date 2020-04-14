/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.domain.exception

class UserNotLogged constructor(message:String): Exception(message) {}

class UserNotFound constructor(message:String): Exception(message) {}