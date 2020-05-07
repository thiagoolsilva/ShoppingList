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
import com.example.domain.shoppinglist.SaveShoppingListNameInteractor
import com.example.presentation.model.ViewState
import com.example.presentation.rules.TestCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NewShoppingListViewModelTest {

    companion object {
        const val NAME = "fake name"
    }

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val testDispacher = TestCoroutineRule()

    @RelaxedMockK
    lateinit var saveShoppingListNameInteractor: SaveShoppingListNameInteractor

    init {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    private val newShoppingListViewModel = NewShoppingListViewModel(saveShoppingListNameInteractor)

    @Before
    fun setup() {
        clearMocks(saveShoppingListNameInteractor)
    }

    @Test
    fun should_saveShoppingList_when_providedValidName() = testDispacher.runBlockingTest {
        coEvery {
            saveShoppingListNameInteractor.saveShoppingListName(any())
        } returns ""

        newShoppingListViewModel.saveShoppingListName(NAME)

        assertEquals(
            newShoppingListViewModel.shoppingListState.value?.status,
            ViewState.Status.SUCCESS
        )
        assertNotNull(
            newShoppingListViewModel.shoppingListState.value?.data
        )
    }

    @Test
    fun should_throwGenericError_when_anUnexpectedErrorHappens() = testDispacher.runBlockingTest {
        coEvery {
            saveShoppingListNameInteractor.saveShoppingListName(any())
        } throws Exception("")

        newShoppingListViewModel.saveShoppingListName(NAME)

        assertEquals(
            newShoppingListViewModel.shoppingListState.value?.status,
            ViewState.Status.ERROR
        )
    }




}