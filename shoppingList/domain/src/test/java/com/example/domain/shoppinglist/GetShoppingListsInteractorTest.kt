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
import com.example.domain.models.ShoppingListItemEntity
import com.example.domain.repository.AuthenticationRepository
import com.example.domain.repository.ShoppingListRepository
import com.example.domain.util.givenDisconnectedUser
import com.example.domain.util.givenValidLoggedUser
import com.example.shared.exception.UserNotLogged
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetShoppingListsInteractorTest {

    @RelaxedMockK
    lateinit var mockShoppingListRepository: ShoppingListRepository

    @RelaxedMockK
    lateinit var mockAuthenticationRepository: AuthenticationRepository<BasicUserInfoEntity>

    init {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    // The use of no stateless class make this test more fast
    private val getShoppingListItemsInteractor: GetShoppingListItemsInteractor =
        GetShoppingListItemsInteractor(mockShoppingListRepository, mockAuthenticationRepository)

    @Before
    fun setup() {
        clearMocks(mockShoppingListRepository, mockAuthenticationRepository)
    }

    @Test
    fun shoud_returnValidShoppingItems_when_existsLoggedUser() {
        givenValidLoggedUser(mockAuthenticationRepository)
        givenValidShoppingItems()

        runBlocking {
            val shoppingItems = getShoppingListItemsInteractor.execute("fake id")

            // then
            assertEquals(shoppingItems.size, 1)
            coVerifyOrder {
                mockAuthenticationRepository.currentUser()
                mockShoppingListRepository.getShoppingListItems(any(), any())
            }
        }
    }

    @Test
    fun should_throwUserNotLogged_when_userIsNotLoggedUser() {
        givenDisconnectedUser(mockAuthenticationRepository)

        runBlocking {
            assertFailsWith<UserNotLogged> {
                getShoppingListItemsInteractor.execute("fake list id")
            }
            coVerify (exactly = 1) {
                mockAuthenticationRepository.currentUser()
            }
            coVerify(exactly = 0) {
                mockShoppingListRepository.getShoppingListItems(any(),any())
            }
        }
    }

    private fun givenValidShoppingItems() {
        coEvery {
            mockShoppingListRepository.getShoppingListItems(any(), any())
        } returns listOf(ShoppingListItemEntity())
    }
}