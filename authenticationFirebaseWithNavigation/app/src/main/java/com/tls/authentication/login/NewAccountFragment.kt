package com.tls.authentication.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tls.authentication.R
import com.google.firebase.auth.FirebaseAuth
import com.tls.authentication.shared.Constants
import kotlinx.android.synthetic.main.new_account.*


class NewAccountFragment : Fragment() {

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
        return inflater.inflate(R.layout.new_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

                    findNavController().popBackStack()
                } else {
                    Log.w(Constants.LOG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        activity,
                        "Failed to create new account",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

}