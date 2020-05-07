/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.shared.exception


class UserNotLogged constructor(message:String): Exception(message)

class UserNotFound constructor(message:String): Exception(message)

class RegistrationNotCompleted constructor(message:String): Exception(message)

class RegistrationWithBadEmail constructor(message:String): Exception(message)

class RegistrationWithBadPassword constructor(message:String): Exception(message)

class InvalidUserPassword constructor(message:String): Exception(message)

class ShoppingOnwerError constructor(message: String) : Exception(message)