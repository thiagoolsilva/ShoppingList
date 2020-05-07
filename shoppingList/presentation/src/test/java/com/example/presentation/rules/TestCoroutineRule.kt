package com.example.presentation.rules

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
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
                Dispatchers.setMain(testDispacher)

                base?.evaluate()

                Dispatchers.resetMain()
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