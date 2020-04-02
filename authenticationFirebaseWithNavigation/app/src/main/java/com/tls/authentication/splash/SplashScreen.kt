package com.tls.authentication.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.tls.authentication.R

class SplashScreen : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.splash_screen_fragment, container, false)
    }


    override fun onResume() {
        super.onResume()

        if (isUserLogged()) {
            findNavController().navigate(SplashScreenDirections.actionSplashScreenToLoggedUserFragment())
        } else {
            findNavController().navigate(SplashScreenDirections.actionSplashScreenToSignInFragment())
        }
    }

    private fun isUserLogged() = auth.currentUser != null

}