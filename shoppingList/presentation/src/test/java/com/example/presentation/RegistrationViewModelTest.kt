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
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
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
