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
import io.mockk.coVerifyOrder
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class UpdateShoppingListItemInteractorTest {

    @RelaxedMockK
    lateinit var mockShoppingListRepository: ShoppingListRepository

    @RelaxedMockK
    lateinit var mockAuthenticationRepository: AuthenticationRepository<BasicUserInfoEntity>

    init {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    // The use of no stateless class make this test more fast
    private val updateShoppingListItemInteractor: UpdateShoppingListItemInteractor =
        UpdateShoppingListItemInteractor(mockShoppingListRepository, mockAuthenticationRepository)

    @Before
    fun setup() {
        clearMocks(mockShoppingListRepository, mockAuthenticationRepository)
    }

    @Test
    fun should_throwUserNotLogged_when_userIsNotLogged() {
        givenDisconnectedUser(mockAuthenticationRepository)

        runBlocking {
            assertFailsWith<UserNotLogged> {
                updateShoppingListItemInteractor.updateShoppingItem(
                    "fake description",
                    false,
                    "fake uuid",
                    "shopping id"
                )
            }

            coVerify(exactly = 1) {
                mockAuthenticationRepository.currentUser()
            }

            coVerify(exactly = 0) {
                mockShoppingListRepository.saveShoppingList(any())
            }
        }
    }

    @Test
    fun should_performedShoppingUpdateAction_when_userIsLogged() {
        givenValidLoggedUser(mockAuthenticationRepository)

        runBlocking {
            updateShoppingListItemInteractor.updateShoppingItem(
                "fake description",
                false,
                "fake uuid",
                "shopping id"
            )

            coVerifyOrder  {
                mockAuthenticationRepository.currentUser()
                mockShoppingListRepository.updateShoppingListItem(any())
            }
        }

    }

}