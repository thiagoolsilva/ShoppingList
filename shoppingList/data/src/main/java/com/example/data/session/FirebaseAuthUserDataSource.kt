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

package com.example.data.session

import com.example.data.util.toBasicUserInfo
import com.example.domain.models.BasicUserInfoEntity
import com.example.domain.repository.AuthenticationRepository
import com.example.shared.exception.*
import com.google.firebase.auth.*
import kotlinx.coroutines.tasks.await
import java.lang.Exception


class FirebaseAuthUserDataSource :
    AuthenticationRepository<BasicUserInfoEntity> {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun isLogged(): Boolean {
        return auth.currentUser != null
    }

    override suspend fun currentUser(): BasicUserInfoEntity? {
        return auth.currentUser?.toBasicUserInfo()
    }

    override suspend fun authenticateUser(email: String, password: String): BasicUserInfoEntity {
        try {
            val userInfo = auth.signInWithEmailAndPassword(email, password)
                .await()
                .user?.toBasicUserInfo()

            if (userInfo != null) return userInfo else throw InvalidUserPassword("invalid user or password")

        } catch (error: Exception) {
            when (error) {
                is FirebaseAuthInvalidUserException -> throw UserNotFound("user not found.")
                is FirebaseAuthInvalidCredentialsException -> throw InvalidUserPassword("invalid password")
                else -> throw error
            }
        }
    }

    override suspend fun signUp(email: String, password: String): BasicUserInfoEntity {
        try {
            val userInfo = auth.createUserWithEmailAndPassword(email, password)
                .await()
                .user?.toBasicUserInfo()

            if (userInfo != null) return userInfo else throw RegistrationNotCompleted("user not created")
        } catch (error: Exception) {
            when (error) {
                is FirebaseAuthWeakPasswordException -> throw RegistrationWithBadPassword("Bad password provided")
                is FirebaseAuthInvalidCredentialsException -> throw RegistrationWithBadEmail("bad email provided")
                else -> throw error
            }
        }
    }

    override suspend fun updateProfile(name: String) {
        if (name.isNotEmpty()) {
            auth.currentUser?.let {
                val userProfileChangeRequest =
                    UserProfileChangeRequest.Builder().setDisplayName(name).build()

                it.updateProfile(userProfileChangeRequest)
                    .await()
            }
        }
    }

    override suspend fun logout() {
        auth.signOut()
    }

}