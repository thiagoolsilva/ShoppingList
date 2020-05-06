/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.tls.firebase.authentication.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.presentation.RegistrationViewModel
import com.example.presentation.model.ViewState
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.tls.firebase.R
import com.tls.firebase.util.disableErrorMessage
import kotlinx.android.synthetic.main.new_account.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class RegistrationUserFragment : Fragment() {

    private val registrationViewModel: RegistrationViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.new_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configViews()
        configViewModel()
    }

    /**
     * Config view model
     */
    private fun configViewModel() {
        registrationViewModel.registrationState.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ViewState.Status.SUCCESS -> {
                    when(it.data) {
                        is RegistrationViewModel.RegistrationState.RegistrationWithInvalidFields -> handleErrorFields(it.data as RegistrationViewModel.RegistrationState.RegistrationWithInvalidFields)
                        is RegistrationViewModel.RegistrationState.RegistrationCompleted -> goToMainScreen()
                        is RegistrationViewModel.RegistrationState.RegistrationWithBadEmail -> showBadEmailErrorMessage()
                        is RegistrationViewModel.RegistrationState.RegistrationWithBadPassword -> showBadPassworddMessage()
                    }
                }
                ViewState.Status.ERROR -> showGenericError()
            }
        })
    }

    /**
     * Show generic error message
     */
    private fun showGenericError() {
        Snackbar.make(parent, getString(R.string.registration_generic_error_message), Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Show bad password error message
     */
    private fun showBadPassworddMessage() {
        Snackbar.make(parent, getString(R.string.registration_bad_password_pattern_message), Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Show Bad email error message
     */
    private fun showBadEmailErrorMessage() {
        Snackbar.make(parent, getString(R.string.registration_bad_email_message), Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Handle error fields events
     */
    private fun handleErrorFields(data: RegistrationViewModel.RegistrationState.RegistrationWithInvalidFields) {
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
        RegistrationViewModel.INPUT_PASSWORD.first to txtFieldPassword,
        RegistrationViewModel.INPUT_USERNAME.first to txtFieldEmail
    )

    /**
     * Config views
     */
    private fun configViews() {
        btnCreateAccount.setOnClickListener {

            val email = txtInputEmail.text.toString()
            val password = txtInputPassword.text.toString()

            createNewUser(email, password)
        }

        txtInputEmail.addTextChangedListener {
            txtFieldEmail.disableErrorMessage()
        }

        txtInputPassword.addTextChangedListener {
            txtFieldPassword.disableErrorMessage()
        }
    }

    /**
     * Go to MainScreen
     */
    private fun goToMainScreen() {
        findNavController().popBackStack(R.id.splashScreen, false)
    }

    /**
     * Create new user
     */
    private fun createNewUser(email: String, password: String) {
        registrationViewModel.registrateUser(email = email, password = password)
    }

}