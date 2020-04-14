/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.tls.authentication.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.presentation.auth.SignInViewModel
import com.example.presentation.model.ViewState
import com.tls.authentication.R
import kotlinx.android.synthetic.main.sign_in_activity.*
import org.koin.android.ext.android.inject

class SignInActivity : AppCompatActivity() {

    private val viewModel: SignInViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in_activity)

        configViews()
        configViewModel()
    }

    /**
     * Config views
     */
    private fun configViews() {
        btnSignOut.setOnClickListener {
            startActivity(Intent(this@SignInActivity, NewAccountActivity::class.java))
        }

        btnSignIn.setOnClickListener {
            signIn()
        }
    }

    /**
     * Config viewModel state
     */
    private fun configViewModel() {
        viewModel.getState().observe(this, Observer {
            if (it.status == ViewState.Status.ERROR) {
                showSignErrorMessage()
            } else if (it.status == ViewState.Status.SUCCESS) {
                goToLoggedScreen()
            }
        })
    }

    /**
     * Show error message to user
     */
    private fun showSignErrorMessage() {
        Toast.makeText(
            this@SignInActivity,
            "Failed to login in firebase. Try again!",
            Toast.LENGTH_LONG
        ).show()
    }

    /**
     * Go to Logged Screen
     */
    private fun goToLoggedScreen() {
        val loggedAct = Intent(this@SignInActivity, LoggedUserActivity::class.java)
        loggedAct.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY
        startActivity(loggedAct)
        finishAffinity()
    }

    /**
     * Sign In user
     */
    private fun signIn() {
        val email = txtInputEmail.text?.toString() ?: ""
        val password = txtInputPassword.text?.toString() ?: ""

        viewModel.authenticateUser(email = email, password = password)
    }

}