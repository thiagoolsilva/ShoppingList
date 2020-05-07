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

package com.example.shared.exception

class UserNotLogged constructor(message: String) : Exception(message)

class UserNotFound constructor(message: String) : Exception(message)

class RegistrationNotCompleted constructor(message: String) : Exception(message)

class RegistrationWithBadEmail constructor(message: String) : Exception(message)

class RegistrationWithBadPassword constructor(message: String) : Exception(message)

class InvalidUserPassword constructor(message: String) : Exception(message)

class ShoppingOnwerError constructor(message: String) : Exception(message)
