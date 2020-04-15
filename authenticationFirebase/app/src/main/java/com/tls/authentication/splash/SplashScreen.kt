/*
 *
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.tls.authentication.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.presentation.SplashViewModel
import com.example.presentation.model.ViewState
import com.tls.authentication.R
import org.koin.android.ext.android.inject

class SplashScreen : Fragment() {

    private val slashViewModel: SplashViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configViewModel()
    }

    private fun configViewModel() {
        slashViewModel.getUserState().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                    ViewState.Status.SUCCESS
                -> if (it.data != null) goToLoggedUserScreen() else goToSignInScreen()
                ViewState.Status.ERROR -> findNavController().navigate(SplashScreenDirections.actionSplashScreenToSignInFragment())
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.splash_screen, container, false)
    }

    override fun onResume() {
        super.onResume()
        slashViewModel.fetchUserState()
    }

    /**
     * Go to logged screen
     */
    private fun goToLoggedUserScreen() {
        findNavController().navigate(SplashScreenDirections.actionSplashScreenToLoggedUserFragment())
    }

    /**
     * Go to sign in screen
     */
    private fun goToSignInScreen() {
        findNavController().navigate(SplashScreenDirections.actionSplashScreenToSignInFragment())
    }
}