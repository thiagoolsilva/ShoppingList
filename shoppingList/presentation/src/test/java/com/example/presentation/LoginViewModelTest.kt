package com.example.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.domain.interactor.auth.LogoutInteractor
import com.example.domain.interactor.auth.SignInUserInteractor
import com.example.domain.models.BasicUserInfoEntity
import com.example.presentation.model.ViewState
import com.example.presentation.rules.TestCoroutineRule
import com.example.shared.exception.InvalidUserPassword
import com.example.shared.exception.UserNotFound
import com.example.shared.exception.UserNotLogged
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    companion object {
        const val EMAIL = "fake email"
        const val PASSWORD = "fake password"
        const val ID = "fake id"
    }

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val testDispacher = TestCoroutineRule()

    @RelaxedMockK
    lateinit var signInUserInteractor: SignInUserInteractor

    @RelaxedMockK
    lateinit var logoutInteractor: LogoutInteractor

    init {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    private val loginViewModel = LoginViewModel(signInUserInteractor, logoutInteractor)

    @Before
    fun setup() {
        clearMocks(signInUserInteractor, logoutInteractor)
    }

    @Test
    fun should_disconnectUser_when_performedValidAction() = testDispacher.runBlockingTest {
        coEvery {
            logoutInteractor.execute()
        } returns Unit

        loginViewModel.disconnect()

        assertEquals(
            loginViewModel.currentLiveState.value?.status,
            ViewState.Status.SUCCESS
        )
        assertTrue(
            loginViewModel.currentLiveState.value?.data is LoginViewModel.AuthenticationState.UserDisconnected
        )
    }

    @Test
    fun should_throwGenericException_when_anUnexpectedErrorHappens() =
        testDispacher.runBlockingTest {
            coEvery {
                logoutInteractor.execute()
            } throws Exception()

            loginViewModel.disconnect()

            assertEquals(
                loginViewModel.currentLiveState.value?.status,
                ViewState.Status.ERROR
            )
        }

    @Test
    fun should_authenticateUser_when_providValidFields() = testDispacher.runBlockingTest {
        coEvery {
            signInUserInteractor.execute(any())
        } returns BasicUserInfoEntity(EMAIL, PASSWORD, ID)

        loginViewModel.authenticateUser(EMAIL, PASSWORD)

        assertEquals(
            loginViewModel.currentLiveState.value?.status,
            ViewState.Status.SUCCESS
        )
        assertTrue(
            loginViewModel.currentLiveState.value?.data is LoginViewModel.AuthenticationState.AuthenticatedUser
        )
    }

    @Test
    fun should_returnUserNotLogged_when_userIsNotLogged() = testDispacher.runBlockingTest {
        coEvery {
            signInUserInteractor.execute(any())
        } throws UserNotLogged("")

        loginViewModel.authenticateUser(EMAIL, PASSWORD)

        assertEquals(
            loginViewModel.currentLiveState.value?.status,
            ViewState.Status.SUCCESS
        )
        assertTrue(
            loginViewModel.currentLiveState.value?.data is LoginViewModel.AuthenticationState.UnauthorizedUser
        )
    }

    @Test
    fun should_returnUserNotFound_when_userNotExists() = testDispacher.runBlockingTest {
        coEvery {
            signInUserInteractor.execute(any())
        } throws UserNotFound("")

        loginViewModel.authenticateUser(EMAIL, PASSWORD)

        assertEquals(
            loginViewModel.currentLiveState.value?.status,
            ViewState.Status.SUCCESS
        )
        assertTrue(
            loginViewModel.currentLiveState.value?.data is LoginViewModel.AuthenticationState.UserNotFound
        )
    }

    @Test
    fun should_returnInvalidUserPassword_when_providedInvalidpassword() = testDispacher.runBlockingTest {
        coEvery {
            signInUserInteractor.execute(any())
        } throws InvalidUserPassword("")

        loginViewModel.authenticateUser(EMAIL, PASSWORD)

        assertEquals(
            loginViewModel.currentLiveState.value?.status,
            ViewState.Status.SUCCESS
        )
        assertTrue(
            loginViewModel.currentLiveState.value?.data is LoginViewModel.AuthenticationState.InvalidUserPassword
        )
    }

    @Test
    fun should_returnInvalidFIelds_when_providedEmptyEmailAndPassword() = testDispacher.runBlockingTest {
        loginViewModel.authenticateUser("", "")

        assertEquals(
            loginViewModel.currentLiveState.value?.status,
            ViewState.Status.ERROR
        )
        assertTrue(
            loginViewModel.currentLiveState.value?.data is LoginViewModel.AuthenticationState.InvalidFields
        )
    }
}