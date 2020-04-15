/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.tls.authentication.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.presentation.SignInViewModel
import com.example.presentation.model.ViewState
import com.tls.authentication.R
import kotlinx.android.synthetic.main.sign_in_activity.*
import org.koin.android.ext.android.inject

class SignInFragment : Fragment() {

    private val viewModel: SignInViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configViews()
        configViewModel()
    }

    /**
     * Config views
     */
    private fun configViews() {
        btnSignOut.setOnClickListener {
            goToNewAccountScreen()
        }

        btnSignIn.setOnClickListener {
            signIn()
        }
    }

    /**
     * Config viewModel state
     */
    private fun configViewModel() {
        viewModel.getState().observe(viewLifecycleOwner, Observer {
            if (it.status == ViewState.Status.ERROR) {
                showSignErrorMessage()
            } else if (it.status == ViewState.Status.SUCCESS) {
                goToLoggedScreen()
            }
        })
    }

    /**
     * Go to new Account Screen
     */
    private fun goToNewAccountScreen() {
        val action = SignInFragmentDirections.actionSignInFragmentToNewAccountFragment()
        findNavController().navigate(action)
    }

    /**
     * Go to logged Screen
     */
    private fun goToLoggedScreen() {
        val action =
            SignInFragmentDirections.actionSignInFragmentToLoggedUserFragment()
        findNavController().navigate(action)
    }

    /**
     * Show error message to user
     */
    private fun showSignErrorMessage() {
        Toast.makeText(
            activity,
            "Failed to login in firebase. Try again!",
            Toast.LENGTH_LONG
        ).show()
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