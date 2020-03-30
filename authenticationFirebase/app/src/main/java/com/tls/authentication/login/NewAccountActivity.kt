package com.tls.authentication.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.tls.authentication.R
import com.google.firebase.auth.FirebaseAuth
import com.tls.authentication.shared.Constants
import kotlinx.android.synthetic.main.new_account_activity.*


class NewAccountActivity : Activity() {

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.new_account_activity)

        auth = FirebaseAuth.getInstance()

        btnCreateAccount.setOnClickListener() {

            val email = txtInputEmail.text.toString()
            val password = txtInputPassword.text.toString()

            createNewUser(email, password)
        }
    }

    private fun createNewUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->

                if (task.isSuccessful) {
                    Log.d(Constants.LOG, "message")
                    val loggedAct = Intent(this@NewAccountActivity, LoggedUserActivity::class.java)
                    loggedAct.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY

                    startActivity(loggedAct)
                    finishAffinity()
                } else {
                    Log.w(Constants.LOG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        this@NewAccountActivity,
                        "Failed to create new account",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

}