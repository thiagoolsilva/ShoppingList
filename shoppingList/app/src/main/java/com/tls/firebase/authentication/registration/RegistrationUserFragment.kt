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
                    when (it.data) {
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
        val validationFields: Map<String, TextInputLayout> = initValidationFields()

        data.fields.forEach {
            val errorMessage = getString(it.second)
            validationFields[it.first]?.error = errorMessage
            Timber.d("Error message [%s]", errorMessage)
        }
    }

    /**
     * Map ViewModel schema with views
     */
    private fun initValidationFields() = mapOf(
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
