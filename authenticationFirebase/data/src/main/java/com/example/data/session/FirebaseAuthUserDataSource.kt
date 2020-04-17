/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.data.session

import com.example.data.util.toBasicUserInfo
import com.example.domain.exception.UserNotFound
import com.example.domain.exception.UserNotLogged
import com.example.domain.models.BasicUserInfoEntity
import com.example.domain.repository.AuthenticationRepository
import com.google.firebase.auth.FirebaseAuth
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


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
        return suspendCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val sessionInfo = auth.currentUser?.toBasicUserInfo()

                        if (sessionInfo != null) continuation.resume(sessionInfo) else continuation.resumeWithException(
                            UserNotFound("User not found")
                        )
                    } else {
                        continuation.resumeWithException(UserNotLogged("User not logged"))
                    }
                }
        }
    }

    override suspend fun signUp(email: String, password: String): BasicUserInfoEntity {
        return suspendCoroutine { continuation ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        val basicUserInfo = auth.currentUser?.toBasicUserInfo()

                        if (basicUserInfo != null) continuation.resume(basicUserInfo) else continuation.resumeWithException(
                            UserNotFound("User not found")
                        )
                    } else {
                        continuation.resumeWithException(UserNotLogged("User not logged"))
                    }
                }
        }
    }

    override suspend fun logout() {
        auth.signOut()
    }

}