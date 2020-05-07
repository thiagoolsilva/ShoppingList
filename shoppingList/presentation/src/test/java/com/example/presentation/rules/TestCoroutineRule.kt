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

package com.example.presentation.rules

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.runBlockingTest
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * This class will create a TestCoroutine rule to handle all coroutines works on unit testing context
 * For more details about about testing coroutines take a look at https://github.com/Kotlin/kotlinx.coroutines/tree/master/kotlinx-coroutines-test
 */
@ExperimentalCoroutinesApi
class TestCoroutineRule : TestRule {

    // this instance could be used to inject Coroutine dispatcher on ViewModel scope
    val testDispacher = TestCoroutineDispatcher()

    // 1. this instance could be used to run blockingTest on unit testing
    // 2. the second form to run blocking test is using extension function runBlockingTest as described below
    val testCoroutineScope = TestCoroutineScope(testDispacher)

    override fun apply(base: Statement?, description: Description?): Statement =
        object : Statement() {
            override fun evaluate() {
                kotlinx.coroutines.Dispatchers.setMain(testDispacher)

                base?.evaluate()

                kotlinx.coroutines.Dispatchers.resetMain()
                testCoroutineScope.cleanupTestCoroutines()
            }
        }

    /**
     * Run blocking Test by provided Coroutine Scope
     * @param block code under test
     */
    fun runBlockingTest(block: suspend TestCoroutineScope.() -> Unit) =
        testCoroutineScope.runBlockingTest { block() }
}
