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
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SaveShoppingListNameInteractorTest {

    private companion object  {
        const val SHOPPING_LIST_ID = "ab339e13-d293-4cc7-945b-ccc913a3a62a"
        const val SHOPPING_LIST_NAME = "fake shopping name"
    }

    @RelaxedMockK
    lateinit var mockShoppingListRepository: ShoppingListRepository

    @RelaxedMockK
    lateinit var mockAuthenticationRepository: AuthenticationRepository<BasicUserInfoEntity>

    init {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    // The use of no stateless class make this test more fast
    private val saveShoppingListNameInteractor: SaveShoppingListNameInteractor =
        SaveShoppingListNameInteractor(mockShoppingListRepository, mockAuthenticationRepository)

    @Before
    fun setup() {
        clearMocks(mockShoppingListRepository, mockAuthenticationRepository)
    }

    @Test
    fun should_returnShoppingId_when_userIsLogged() {
        givenValidLoggedUser(mockAuthenticationRepository)
        givenValidShoppingListId()

        runBlocking {
            val shoppinglistId = saveShoppingListNameInteractor.saveShoppingListName(
                SHOPPING_LIST_NAME)

            coVerify(exactly = 1) {
                mockAuthenticationRepository.currentUser()
            }
            coVerify(exactly = 1) {
                mockShoppingListRepository.saveShoppingList(any())
            }
            assertEquals(shoppinglistId, SHOPPING_LIST_ID)
        }
    }

    @Test
    fun should_throwUserNotLogged_when_userIsNotLogged() {
        givenDisconnectedUser(mockAuthenticationRepository)

        runBlocking {
           assertFailsWith <UserNotLogged> {
               saveShoppingListNameInteractor.saveShoppingListName(SHOPPING_LIST_NAME)
           }

            coVerify(exactly = 1) {
                mockAuthenticationRepository.currentUser()
            }

            coVerify(exactly = 0) {
                mockShoppingListRepository.saveShoppingList(any())
            }
        }
    }

    private fun givenValidShoppingListId() {
        coEvery {
            mockShoppingListRepository.saveShoppingList(any())
        } returns SHOPPING_LIST_ID
    }

}