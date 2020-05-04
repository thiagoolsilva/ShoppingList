/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.domain.models.ShoppingListEntity
import com.example.domain.shoppinglist.GetShoppingListsInteractor
import com.example.presentation.model.ViewState
import com.example.presentation.rules.TestCoroutineRule
import com.example.shared.exception.UserNotLogged
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
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