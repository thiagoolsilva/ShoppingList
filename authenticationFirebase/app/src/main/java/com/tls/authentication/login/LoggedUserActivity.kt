/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.tls.authentication.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.tls.authentication.MainActivity
import com.tls.authentication.R
import com.tls.authentication.util.toEditable
import kotlinx.android.synthetic.main.logged_user_activity.*


class LoggedUserActivity : Activity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.logged_user_activity)
        auth = FirebaseAuth.getInstance()

        btnSignOut.setOnClickListener {
            signOutUser()
        }
    }


    override fun onResume() {
        super.onResume()

        updateUI()
    }


    private fun updateUI() {
        val currentUser = auth.currentUser

        currentUser?.let {
            val userName = currentUser.displayName
            val email = currentUser.email

            userName?.let {
                TxtInputUsername.text = it.toEditable()
            }
            email?.let {
                txtInputEmail.text = it.toEditable()
            }

        } ?: let {
            signOutUser()
        }
    }

    private fun signOutUser() {
        auth.signOut()
        startActivity(Intent(this@LoggedUserActivity, MainActivity::class.java))
    }

}