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

package com.example.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.domain.models.ShoppingListEntity
import com.example.domain.shoppinglist.GetShoppingListsInteractor
import com.example.presentation.model.ViewState
import com.example.presentation.rules.TestCoroutineRule
import com.example.shared.exception.UserNotLogged
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShoppingListItemViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val testDispacher = TestCoroutineRule()

    @RelaxedMockK
    lateinit var getShoppingListsInteractor: GetShoppingListsInteractor

    init {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    private var shoppingListViewModel = ShoppingListViewModel(getShoppingListsInteractor)

    @Before
    fun setup() {
        clearMocks(
            getShoppingListsInteractor
        )
    }

    @Test
    fun should_returnValidShoppingList_when_userIsLogged() = testDispacher.runBlockingTest {
        coEvery {
            getShoppingListsInteractor.execute()
        } returns listOf(ShoppingListEntity())

        shoppingListViewModel.fetchShoppingList()

        Assert.assertEquals(
            shoppingListViewModel.shoppingListState.value?.status,
            ViewState.Status.SUCCESS
        )
        coVerify(exactly = 1) {
            getShoppingListsInteractor.execute()
        }
    }

    @Test
    fun should_throwUserNotLogged_when_userIsNotLogged() = testDispacher.runBlockingTest {
        coEvery {
            getShoppingListsInteractor.execute()
        } throws UserNotLogged("user not logged")

        shoppingListViewModel.fetchShoppingList()

        Assert.assertEquals(
            shoppingListViewModel.shoppingListState.value?.status,
            ViewState.Status.ERROR
        )
    }
}
