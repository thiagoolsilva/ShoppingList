/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.data.session

import com.example.data.model.SessionInfo
import com.example.domain.repository.AuthenticationRepository

class FirebaseAuthUserDataSource :
    AuthenticationRepository<SessionInfo> {

    override fun authenticateUser(name: String, password: String): SessionInfo? {
        TODO("Not yet implemented")
    }

    override fun isLogged(): Boolean {
        TODO("Not yet implemented")
    }

    override fun signUp(name: String, password: String): SessionInfo? {
        TODO("Not yet implemented")
    }

    override fun logout() {
        TODO("Not yet implemented")
    }

    override fun currentUser(): SessionInfo? {
        TODO("Not yet implemented")
    }

}