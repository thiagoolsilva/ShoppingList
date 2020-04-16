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
import com.example.presentation.NewAccountViewModel
import com.example.presentation.model.ViewState
import com.tls.authentication.R
import kotlinx.android.synthetic.main.new_account.*
import org.koin.android.ext.android.inject

class NewAccountFragment : Fragment() {

    private val newAccountViewModel: NewAccountViewModel by inject()

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
        newAccountViewModel.getSignUpState().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ViewState.Status.SUCCESS -> showUserCreatedMessage()
                ViewState.Status.ERROR -> showErrorUserMessage()
            }
        })
    }

    /**
     * Config views
     */
    private fun configViews() {
        btnCreateAccount.setOnClickListener() {

            val email = txtInputEmail.text.toString()
            val password = txtInputPassword.text.toString()

            createNewUser(email, password)
        }
    }

    /**
     * Show error user message
     */
    private fun showErrorUserMessage() {
        Toast.makeText(activity, "User not created. Try again!", Toast.LENGTH_SHORT).show()
    }

    /**
     * Show user creation message
     */
    fun showUserCreatedMessage() {
        Toast.makeText(activity, "User created successfully", Toast.LENGTH_SHORT).show()
        findNavController().popBackStack()
    }

    /**
     * Create new user
     */
    private fun createNewUser(email: String, password: String) {
        newAccountViewModel.signUp(email = email, password = password)
    }
}