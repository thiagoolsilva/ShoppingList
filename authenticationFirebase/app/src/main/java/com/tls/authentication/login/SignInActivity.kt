package com.tls.authentication.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.tls.authentication.R
import com.tls.authentication.shared.Constants
import kotlinx.android.synthetic.main.sign_in_activity.*

class SignInActivity : Activity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.sign_in_activity)

        btnSignOut.setOnClickListener {
            startActivity(Intent(this@SignInActivity, NewAccountActivity::class.java))
         }


        btnSignIn.setOnClickListener {
            signIn()
        }
    }


    private fun signIn() {
        val auth = FirebaseAuth.getInstance()

        val email = txtInputEmail.text?.toString()
        val password = txtInputPassword.text?.toString()

        if (email != null && password != null) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        startActivity(Intent(this@SignInActivity, LoggedUserActivity::class.java))
                        finish()
                    } else {
                        Log.w(Constants.LOG, "signIn:failure", it.exception)

                        Toast.makeText(
                            this@SignInActivity,
                            "Failed to login in firebase. Try again!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }


    }
}