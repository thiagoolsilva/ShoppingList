package com.example.domain.util

import com.example.domain.models.BasicUserInfoEntity
import com.example.domain.repository.AuthenticationRepository
import io.mockk.coEvery

/**
 * Configure authentication mock object to returns a valid logged user
 */
fun givenValidLoggedUser(authentication: AuthenticationRepository<BasicUserInfoEntity>) {
    coEvery {
        authentication.currentUser()
    } returns BasicUserInfoEntity("fake email", "fake name", "fake id")
}

/**
 * Configure authentication mock object to return a unauthorized user
 */
fun givenDisconnectedUser(authentication: AuthenticationRepository<BasicUserInfoEntity>) {
    coEvery {
        authentication.currentUser()
    } returns null
}