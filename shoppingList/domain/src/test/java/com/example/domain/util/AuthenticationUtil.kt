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

package com.example.domain.util

import com.example.domain.models.BasicUserInfoEntity
import com.example.domain.repository.AuthenticationRepository
import io.mockk.coEvery

/**
 * Configure authentication mock object to returns a valid logged user
 */
fun givenValidLoggedUser(authentication: AuthenticationRepository<BasicUserInfoEntity>) {
    coEvery {
        authentication.currentUser()
    } returns BasicUserInfoEntity("fake email", "fake name", "fake id")
}

/**
 * Configure authentication mock object to return a unauthorized user
 */
fun givenDisconnectedUser(authentication: AuthenticationRepository<BasicUserInfoEntity>) {
    coEvery {
        authentication.currentUser()
    } returns null
}