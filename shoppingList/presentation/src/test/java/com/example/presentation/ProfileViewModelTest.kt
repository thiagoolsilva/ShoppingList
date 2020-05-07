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
import com.example.domain.interactor.auth.GetLoggedUserInteractor
import com.example.domain.interactor.auth.UpdateProfileInfoInteractor
import com.example.domain.models.BasicUserInfoEntity
import com.example.presentation.model.ViewState
import com.example.presentation.rules.TestCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProfileViewModelTest {

    companion object {
        const val EMAIL = "fake email"
        const val NAME = "fake name"
        const val ID = "fakeId"
    }

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val testDispacher = TestCoroutineRule()

    @RelaxedMockK
    lateinit var getLoggedUserInteractor: GetLoggedUserInteractor

    @RelaxedMockK
    lateinit var updateProfileInfoInteractor: UpdateProfileInfoInteractor

    init {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    private val profileViewModel =
        ProfileViewModel(getLoggedUserInteractor, updateProfileInfoInteractor)


    @Before
    fun setup() {
        clearMocks(
            getLoggedUserInteractor,
            updateProfileInfoInteractor
        )
    }

    @Test
    fun should_returnValidProfile_when_userIsLogged() = testDispacher.runBlockingTest {
        coEvery {
            getLoggedUserInteractor.execute()
        } returns BasicUserInfoEntity(EMAIL, NAME, ID)

        profileViewModel.fetchUserProfile()

        assertEquals(
            profileViewModel.currentUserState.value?.status,
            ViewState.Status.SUCCESS
        )
        assertNotNull(
            profileViewModel.currentUserState.value?.data
        )
        assertTrue(
            profileViewModel.currentUserState.value?.data is ProfileViewModel.UserPofileState.CollectedProfileState
        )
    }

    @Test
    fun should_throwGenericError_when_anUnexpectedErrorHappens() = testDispacher.runBlockingTest {
        coEvery {
            getLoggedUserInteractor.execute()
        } throws Exception("")

        profileViewModel.fetchUserProfile()

        assertEquals(
            profileViewModel.currentUserState.value?.status,
            ViewState.Status.ERROR
        )
    }

    @Test
    fun should_updateProfile_when_providedValidName() = testDispacher.runBlockingTest {
        coEvery {
            updateProfileInfoInteractor.execute(any())
        } returns Unit

        profileViewModel.updateUserProfile(NAME)

        assertEquals(
            profileViewModel.currentUserState.value?.status,
            ViewState.Status.SUCCESS
        )

        assertTrue(
            profileViewModel.currentUserState.value?.data is ProfileViewModel.UserPofileState.UpdatedProfileState
        )
    }

    @Test
    fun should_notUpdateProfile_when_anUnexpectedErrorHappens() = testDispacher.runBlockingTest {
        coEvery {
            updateProfileInfoInteractor.execute(any())
        } throws Exception("")

        profileViewModel.updateUserProfile(NAME)

        assertEquals(
            profileViewModel.currentUserState.value?.status,
            ViewState.Status.ERROR
        )
    }



}