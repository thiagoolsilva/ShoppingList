/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.domain.shoppinglist

import com.example.domain.models.BasicUserInfoEntity
import com.example.domain.repository.AuthenticationRepository
import com.example.domain.repository.ShoppingListRepository
import com.example.shared.exception.UserNotLogged
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.coEvery
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
        givenAValidLoggedUser()

        // when
        runBlocking {
            getShoppingListsInteractor.execute()

            // then
            coVerify(exactly = 1) { mockAuthenticationRepository.currentUser() }
        }
    }

    @Test
    fun should_throwException_when_notExistsLoggedUser() {
        // given
        givenDisconnectedUser()

        // when
        runBlocking {
            assertFailsWith<UserNotLogged> {
                getShoppingListsInteractor.execute()

                // then
                // catch UserNotLogged exception
            }
        }
    }

    private fun givenDisconnectedUser() {
        coEvery {
            mockAuthenticationRepository.currentUser()
        } returns null
    }

    private fun givenAValidLoggedUser() {
        coEvery {
            mockAuthenticationRepository.currentUser()
        } returns BasicUserInfoEntity("fake email", "fake name", "fake id")
    }

}