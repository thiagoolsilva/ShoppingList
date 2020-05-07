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
import com.example.domain.models.ShoppingListItemEntity
import com.example.domain.shoppinglist.GetShoppingListItemsInteractor
import com.example.domain.shoppinglist.SaveShoppingListItemInteractor
import com.example.domain.shoppinglist.UpdateShoppingListItemInteractor
import com.example.presentation.model.ViewState
import com.example.presentation.rules.TestCoroutineRule
import com.example.shared.exception.ShoppingOnwerError
import com.example.shared.exception.UserNotLogged
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShoppingListViewModelTest {

    companion object {
        const val ITEM_DESCRIPTION = "shopping item description"
        const val SHOPPING_ITEM_ID = "shopping item id"
        const val SHOPPING_UUID = "fake uuid"
    }

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val testDispacher = TestCoroutineRule()

    @RelaxedMockK
    lateinit var getShoppingListItemsInteractor: GetShoppingListItemsInteractor

    @RelaxedMockK
    lateinit var saveShoppingListItemInteractor: SaveShoppingListItemInteractor

    @RelaxedMockK
    lateinit var updateShoppingListItemInteractor: UpdateShoppingListItemInteractor

    init {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    private val shoppingListViewModel = ShoppingListItemViewModel(
        getShoppingListItemsInteractor,
        saveShoppingListItemInteractor,
        updateShoppingListItemInteractor
    )

    @Before
    fun setup() {
        clearMocks(
            getShoppingListItemsInteractor,
            saveShoppingListItemInteractor,
            updateShoppingListItemInteractor
        )
    }

    @Test
    fun should_returnSucess_when_savedValidShoppingList() = testDispacher.runBlockingTest {
        coEvery {
            saveShoppingListItemInteractor.execute(any(), any())
        } returns ""

        shoppingListViewModel.saveShoppingListItem(ITEM_DESCRIPTION, SHOPPING_ITEM_ID)

        assertEquals(
            ViewState.Status.SUCCESS,
            shoppingListViewModel.saveShoppingListItemState.value?.status
        )
    }

    @Test
    fun should_throwShoppingOwnerError_when_currentUserNotBelongsToShoppingList() =
        testDispacher.runBlockingTest {
            coEvery {
                saveShoppingListItemInteractor.execute(any(), any())
            } throws ShoppingOnwerError("invalid shopping owner error")

            shoppingListViewModel.saveShoppingListItem(ITEM_DESCRIPTION, SHOPPING_ITEM_ID)

            assertEquals(
                ViewState.Status.ERROR,
                shoppingListViewModel.saveShoppingListItemState.value?.status
            )
            assertNull(shoppingListViewModel.saveShoppingListItemState.value?.data)
        }

    @Test
    fun should_updateShoppingListWithSucess_when_providedValidFields() =
        testDispacher.runBlockingTest {
            coEvery {
                getShoppingListItemsInteractor.execute(any())
            } returns listOf(ShoppingListItemEntity())

            shoppingListViewModel.getShoppingItems("")

            assertEquals(
                ViewState.Status.SUCCESS,
                shoppingListViewModel.shoppingListItemsState.value?.status
            )
            assertEquals(
                1,
                shoppingListViewModel.shoppingListItemsState.value?.data?.size
            )
        }

    @Test
    fun should_throwException_when_userIsNotLogged() =
        testDispacher.runBlockingTest {
            coEvery {
                getShoppingListItemsInteractor.execute(any())
            } throws UserNotLogged("user is not logged")

            shoppingListViewModel.getShoppingItems("")

            assertEquals(
                ViewState.Status.ERROR,
                shoppingListViewModel.shoppingListItemsState.value?.status
            )
            assertNull(shoppingListViewModel.shoppingListItemsState.value?.data)
        }

    @Test
    fun should_updateShoppingListItem_when_providedValidFields() = testDispacher.runBlockingTest {
        coEvery {
            updateShoppingListItemInteractor.updateShoppingItem(any(), any(), any(), any())
        } returns Unit

        shoppingListViewModel.updateShoppingListItem(
            ITEM_DESCRIPTION,
            SHOPPING_ITEM_ID,
            true,
            SHOPPING_UUID
        )

        assertEquals(
            ViewState.Status.SUCCESS,
            shoppingListViewModel.updateShoppingListItemState.value?.status
        )
    }

    @Test
    fun should_updateShoppingListItem_when_userIsNotLogged() =
        testDispacher.runBlockingTest {
            coEvery {
                getShoppingListItemsInteractor.execute(any())
            } throws UserNotLogged("user is not logged")

            shoppingListViewModel.getShoppingItems("")

            assertEquals(
                ViewState.Status.ERROR,
                shoppingListViewModel.shoppingListItemsState.value?.status
            )
            assertNull(shoppingListViewModel.shoppingListItemsState.value?.data)
        }


}