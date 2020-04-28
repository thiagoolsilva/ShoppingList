/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.tls.authentication.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.presentation.ProfileViewModel
import com.example.presentation.model.UserInfoView
import com.example.presentation.model.ViewState
import com.tls.authentication.R
import com.tls.authentication.util.toEditable
import kotlinx.android.synthetic.main.logged_user.*
import org.koin.android.ext.android.inject

class ProfileFragment : Fragment() {

    private val profileViewModel: ProfileViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.logged_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configViews()
        configViewModels()
    }

    override fun onResume() {
        super.onResume()

        profileViewModel.fetchUserState()
    }

    /**
     * Config View Models
     */
    private fun configViewModels() {
        profileViewModel.currentUserState.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ViewState.Status.SUCCESS -> updateUI(it.data)
            }
        })
    }

    /**
     * Config views events
     */
    private fun configViews() {
        // TBD
    }

    /**
     * Update screen with user information
     */
    private fun updateUI(data: UserInfoView?) {
        if (data != null) {
            val userName = data.name
            val email = data.email

            TxtInputUsername.text = userName.toEditable()
            txtInputEmail.text = email.toEditable()
        }
    }

    /**
     * Show error message to user
     */
    private fun showErrorMessage() {
        Toast.makeText(activity, "Try again", Toast.LENGTH_SHORT).show()
    }

}
