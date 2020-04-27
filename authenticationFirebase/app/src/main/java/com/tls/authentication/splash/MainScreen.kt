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
import androidx.navigation.fragment.findNavController
import com.tls.authentication.R
import kotlinx.android.synthetic.main.splash_screen.*

class MainScreen : Fragment() {

//    private val slashViewModel: SplashViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        configViewModel()

        startFlow.setOnClickListener {
            val direction = MainScreenDirections.actionSplashScreenToShoppingList()
            findNavController().navigate(direction)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.splash_screen, container, false)
    }

//    /**
//     * Config splash view model
//     */
//    private fun configViewModel() {
//        slashViewModel.getUserState().observe(this, Observer {
//            when (it.status) {
//                ViewState.Status.SUCCESS -> if (it.data == null) goToLoginScreen()
////                ViewState.Status.ERROR -> findNavController().navigate(SplashScreenDirections.actionSplashScreenToSignInFragment())
//            }
//        })
//    }

//    /**
//     * Go to sign in screen
//     */
//    private fun goToLoginScreen() {
//            findNavController().navigate(R.id.signInFragment)
//    }

}