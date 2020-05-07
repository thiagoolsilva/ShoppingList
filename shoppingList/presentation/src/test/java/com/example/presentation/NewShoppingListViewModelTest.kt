/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
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