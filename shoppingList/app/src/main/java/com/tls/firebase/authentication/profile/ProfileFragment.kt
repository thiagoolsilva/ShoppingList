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

package com.tls.firebase.authentication.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.MenuInflater
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.presentation.ProfileViewModel
import com.example.presentation.model.ViewState
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.tls.firebase.R
import com.tls.firebase.util.disableErrorMessage
import com.tls.firebase.util.toEditable
import kotlinx.android.synthetic.main.logged_user.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class ProfileFragment : Fragment() {

    private val profileViewModel: ProfileViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // set fragment to handle onCreateOptionMenu event
        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.logged_user, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_done -> updateProfileName()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configViewModels()
        configViews()
    }

    /**
     * Config views
     */
    private fun configViews() {
        TxtInputUsername.addTextChangedListener {
            txtFieldEmail.disableErrorMessage()
        }
    }

    override fun onResume() {
        super.onResume()

        profileViewModel.fetchUserProfile()
    }

    /**
     * Config View Models
     */
    private fun configViewModels() {
        profileViewModel.currentUserState.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ViewState.Status.SUCCESS -> when (it.data) {
                    is ProfileViewModel.UserPofileState.CollectedProfileState -> updateUI(it.data as ProfileViewModel.UserPofileState.CollectedProfileState)
                    is ProfileViewModel.UserPofileState.UpdatedProfileState -> goToShoppingList()
                    is ProfileViewModel.UserPofileState.InvalidProfileFieldsState -> handleErrorFields(it.data as ProfileViewModel.UserPofileState.InvalidProfileFieldsState)
                }
                ViewState.Status.ERROR -> showGenericErrorMessage(it.error)
            }
        })
    }

    /**
     * Handle error fields events
     */
    private fun handleErrorFields(data: ProfileViewModel.UserPofileState.InvalidProfileFieldsState) {
        val validationFields: Map<String, TextInputLayout> = initValidationFields()

        data.fields.forEach {
            val invalidErrorMessage = getString(it.second)
            validationFields[it.first]?.error = invalidErrorMessage
            Timber.d("Error message [%s]", invalidErrorMessage)
        }
    }

    /**
     * Map ViewModel schema with views
     */
    private fun initValidationFields() = mapOf(
        ProfileViewModel.INPUT_USERNAME.first to txtFieldEmail
    )

    /**
     * Show generic error message
     */
    private fun showGenericErrorMessage(error: Throwable?) {
        error.let { Timber.e(error) }
        Snackbar.make(parent, getString(R.string.profile_generic_error_message), Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Update screen with user information
     */
    private fun updateUI(data: ProfileViewModel.UserPofileState.CollectedProfileState) {
        val userInfo = data.userInfoView

        userInfo?.let {
            val userName = it.name
            val email = it.email

            TxtInputUsername.text = userName.toEditable()
            txtInputEmail.text = email.toEditable()
        }
    }

    /**
     * update profile name
     */
    private fun updateProfileName() {
        val userProfileName = TxtInputUsername.text.toString()
        profileViewModel.updateUserProfile(userProfileName)
    }

    /**
     * Go to shopping list screen
     */
    private fun goToShoppingList() {
        findNavController().popBackStack()
    }
}
