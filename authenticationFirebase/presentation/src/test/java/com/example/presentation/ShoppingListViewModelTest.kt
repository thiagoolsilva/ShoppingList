package com.example.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.domain.shoppinglist.GetShoppingListItemsInteractor
import com.example.domain.shoppinglist.SaveShoppingListItemInteractor
import com.example.domain.shoppinglist.UpdateShoppingListItemInteractor
import com.example.presentation.model.ViewState
import com.example.presentation.rules.TestCoroutineRule
import com.example.shared.exception.ShoppingOnwerError
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

class ShoppingListViewModelTest {

    companion object {
        const val ITEM_DESCRIPTION = "shopping item description"
        const val SHOPPING_ITEM_ID = "shopping item id"
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
    fun should_throwShoppingOwnerError_when_currentUserNotBelongsToShoppingList() = testDispacher.runBlockingTest {

            coEvery {
                saveShoppingListItemInteractor.execute(any(), any())
            }  throws ShoppingOnwerError("invalid shopping owner error")

            shoppingListViewModel.saveShoppingListItem(ITEM_DESCRIPTION, SHOPPING_ITEM_ID)

            assertEquals(
                ViewState.Status.ERROR,
                shoppingListViewModel.saveShoppingListItemState.value?.status
            )

        }

}