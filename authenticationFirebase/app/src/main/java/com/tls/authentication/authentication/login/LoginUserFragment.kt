/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.tls.authentication.authentication.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.presentation.LoginViewModel
import com.example.presentation.model.ViewState
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.tls.authentication.R
import com.tls.authentication.util.disableErrorMessage
import kotlinx.android.synthetic.main.sign_in.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class LoginUserFragment : Fragment() {

    private val viewModel: LoginViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true);
        configViews()
        configViewModel()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().popBackStack(R.id.splashScreen, false)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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

        txtInputEmail.addTextChangedListener {
            txtFieldEmail.disableErrorMessage()
        }

        txtInputPassword.addTextChangedListener {
            txtFieldPassword.disableErrorMessage()
        }



        // configure back press event
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack(R.id.splashScreen, false)
        }
    }

    /**
     * Config viewModel state
     */
    private fun configViewModel() {
        viewModel.currentLiveState.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ViewState.Status.SUCCESS -> when (it.data) {
                    is LoginViewModel.AuthenticationState.AuthenticatedUser -> goToLoggedScreen()
                    is LoginViewModel.AuthenticationState.UnauthorizedUser -> showUnauthorizedUserMessage()
                    is LoginViewModel.AuthenticationState.UserNotFound -> showUserNotFoundMessage()
                    is LoginViewModel.AuthenticationState.InvalidUserPassword -> showInvalidPasswordError()
                }
                ViewState.Status.ERROR -> when(it.data) {
                    // smart cast are not allowed here once the class InvalidFields is declared in another module
                    is LoginViewModel.AuthenticationState.InvalidFields -> handleErrorFields(it.data as LoginViewModel.AuthenticationState.InvalidFields)
                    else -> showSignErrorMessage()
                }
            }
        })
    }

    /**
     * Handle error fields events
     */
    private fun handleErrorFields(data: LoginViewModel.AuthenticationState.InvalidFields) {
        val validationFields:Map<String, TextInputLayout> = initValidationFields()

        data.fields.forEach {
            validationFields[it.first]?.error = it.second
            Timber.d("Error message [%s]", it.second)
        }
    }

    /**
     * Map ViewModel schema with views
     */
    private fun initValidationFields()  = mapOf(
        LoginViewModel.INPUT_PASSWORD.first to txtFieldPassword,
        LoginViewModel.INPUT_USERNAME.first to txtFieldEmail
    )

    /**
     * Show invalid password error message
     */
    private fun showInvalidPasswordError() {
        Snackbar.make(parent, "Invalid login or password", Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Show user not found error message
     */
    private fun showUserNotFoundMessage() {
        Snackbar.make(parent, "User not found", Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Show unauthorized error message
     */
    private fun showUnauthorizedUserMessage() {
        Snackbar.make(parent, "Wrong login or password", Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Show error message to user
     */
    private fun showSignErrorMessage() {
        Snackbar.make(parent, "It was not possible to established a valid connection. Try again!", Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Sign In user
     */
    private fun signIn() {
        val email = txtInputEmail.text?.toString() ?: ""
        val password = txtInputPassword.text?.toString() ?: ""

        viewModel.authenticateUser(email = email, password = password)
    }

    /**
     * Go to new Account Screen
     */
    private fun goToNewAccountScreen() {
        val action = LoginUserFragmentDirections.actionSignInFragmentToRegistrationFragment()
        findNavController().navigate(action)
    }

    /**
     * Go to logged screen
     */
    private fun goToLoggedScreen() {
        findNavController().popBackStack(R.id.splashScreen, false)
    }

}