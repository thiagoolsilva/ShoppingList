package com.example.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShoppingListViewModelTest {

    @Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    val testDispacher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispacher)
    }

    @Test
    fun testCoRoutines() {

    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispacher.cleanupTestCoroutines()
    }
}