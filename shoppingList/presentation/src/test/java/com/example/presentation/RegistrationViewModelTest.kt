/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.domain.interactor.auth.SignUpInteractor
import com.example.domain.models.BasicUserInfoEntity
import com.example.presentation.model.ViewState
import com.example.presentation.rules.TestCoroutineRule
import com.example.shared.exception.RegistrationWithBadEmail
import com.example.shared.exception.RegistrationWithBadPassword
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RegistrationViewModelTest {

    companion object {
        const val EMAIL = "fakeEmail@gmail.com"
        const val PASSWORD = "fakePassword123"
        const val ID = "fakeId"
    }

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val testDispacher = TestCoroutineRule()

    @RelaxedMockK
    lateinit var signUpInteractor: SignUpInteractor

    init {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    private val registrationViewModel = RegistrationViewModel(signUpInteractor)

    @Before
    fun setup() {
        clearMocks(
            signUpInteractor
        )
    }

    @Test
    fun should_registerUser_when_providedValidFields() = testDispacher.runBlockingTest {
        coEvery {
            signUpInteractor.execute(any())
        } returns BasicUserInfoEntity(EMAIL, PASSWORD, ID)

        registrationViewModel.registrateUser(EMAIL, PASSWORD)

        assertEquals(
            registrationViewModel.registrationState.value?.status,
            ViewState.Status.SUCCESS
        )
        assertNotNull(registrationViewModel.registrationState.value?.data)
    }

    @Test
    fun should_returnRegistrationWithInvalidFields_when_providedEmptyEmail() =
        testDispacher.runBlockingTest {
            registrationViewModel.registrateUser("", PASSWORD)

            assertEquals(
                registrationViewModel.registrationState.value?.status,
                ViewState.Status.SUCCESS
            )

            assertTrue(
                registrationViewModel.registrationState.value?.data is RegistrationViewModel.RegistrationState.RegistrationWithInvalidFields
            )
        }

    @Test
    fun should_returnRegistrationWithInvalidFields_when_providedEmptyPassword() =
        testDispacher.runBlockingTest {
            registrationViewModel.registrateUser(EMAIL, "")

            assertEquals(
                registrationViewModel.registrationState.value?.status,
                ViewState.Status.SUCCESS
            )

            assertTrue(
                registrationViewModel.registrationState.value?.data is RegistrationViewModel.RegistrationState.RegistrationWithInvalidFields
            )
        }

    @Test
    fun should_returnRegistrationWithBadEmail_when_providedInvalidEmail() =
        testDispacher.runBlockingTest {
            coEvery {
                signUpInteractor.execute(any())
            } throws RegistrationWithBadEmail("")

            registrationViewModel.registrateUser(EMAIL, PASSWORD)

            assertEquals(
                registrationViewModel.registrationState.value?.status,
                ViewState.Status.SUCCESS
            )

            assertTrue(
                registrationViewModel.registrationState.value?.data is RegistrationViewModel.RegistrationState.RegistrationWithBadEmail
            )
        }

    @Test
    fun should_returnRegistrationWithBadPassword_when_providedInvalidEmail() =
        testDispacher.runBlockingTest {
            coEvery {
                signUpInteractor.execute(any())
            } throws RegistrationWithBadPassword("")

            registrationViewModel.registrateUser(EMAIL, PASSWORD)

            assertEquals(
                registrationViewModel.registrationState.value?.status,
                ViewState.Status.SUCCESS
            )

            assertTrue(
                registrationViewModel.registrationState.value?.data is RegistrationViewModel.RegistrationState.RegistrationWithBadPassword
            )
        }

    @Test
    fun should_returnGenericException_when_anUnexpectedErrorHappens() =
        testDispacher.runBlockingTest {
            coEvery {
                signUpInteractor.execute(any())
            } throws Exception("")

            registrationViewModel.registrateUser(EMAIL, PASSWORD)

            assertEquals(
                registrationViewModel.registrationState.value?.status,
                ViewState.Status.ERROR
            )

            assertNull(
                registrationViewModel.registrationState.value?.data
            )
        }
}