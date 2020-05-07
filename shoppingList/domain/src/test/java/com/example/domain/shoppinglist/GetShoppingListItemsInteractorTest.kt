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

package com.example.domain.shoppinglist

import com.example.domain.models.BasicUserInfoEntity
import com.example.domain.repository.AuthenticationRepository
import com.example.domain.repository.ShoppingListRepository
import com.example.domain.util.givenDisconnectedUser
import com.example.domain.util.givenValidLoggedUser
import com.example.shared.exception.UserNotLogged
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class GetShoppingListItemsInteractorTest {

    @RelaxedMockK
    lateinit var mockShoppingListRepository: ShoppingListRepository

    @RelaxedMockK
    lateinit var mockAuthenticationRepository: AuthenticationRepository<BasicUserInfoEntity>

    init {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    // The use of no stateless class make this test more fast
    private val getShoppingListsInteractor =
        GetShoppingListsInteractor(mockShoppingListRepository, mockAuthenticationRepository)

    @Before
    fun setup() {
        clearMocks(mockShoppingListRepository, mockAuthenticationRepository)
    }

    @Test
    fun should_returnValidList_when_existsLoggedUser() {
        // given
        givenValidLoggedUser(mockAuthenticationRepository)

        // when
        runBlocking {
            getShoppingListsInteractor.execute()

            // then
            coVerify(exactly = 1) { mockAuthenticationRepository.currentUser() }
        }
    }

    @Test
    fun should_throwException_when_userIsNotLoggedUser() {
        // given
        givenDisconnectedUser(mockAuthenticationRepository)

        // when
        runBlocking {
            assertFailsWith<UserNotLogged> {
                getShoppingListsInteractor.execute()

                // then
                // catch UserNotLogged exception
            }
        }
    }

}