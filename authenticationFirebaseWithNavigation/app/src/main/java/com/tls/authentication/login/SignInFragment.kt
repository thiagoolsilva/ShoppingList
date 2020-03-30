package com.tls.authentication.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.tls.authentication.R
import com.tls.authentication.shared.Constants
import kotlinx.android.synthetic.main.sign_in_activity.*

class SignInFragment : Fragment() {

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
        return inflater.inflate(R.layout.sign_in_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSignOut.setOnClickListener {
            startActivity(Intent(activity, NewAccountFragment::class.java))
        }


        btnSignIn.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val email = txtInputEmail.text?.toString()
        val password = txtInputPassword.text?.toString()

        if (email != null && password != null) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val loggedAct = Intent(activity, LoggedUserFragment::class.java)
                        loggedAct.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY
                        startActivity(loggedAct)

                    } else {
                        Log.w(Constants.LOG, "signIn:failure", it.exception)

                        Toast.makeText(
                            activity,
                            "Failed to login in firebase. Try again!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }


    }
}