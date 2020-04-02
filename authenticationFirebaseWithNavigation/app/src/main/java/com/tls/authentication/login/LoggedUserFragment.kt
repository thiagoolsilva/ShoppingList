package com.tls.authentication.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.tls.authentication.MainActivity
import com.tls.authentication.R
import com.tls.authentication.shared.Constants
import com.tls.authentication.shared.toEditable
import kotlinx.android.synthetic.main.logged_user_activity.*


class LoggedUserFragment : Fragment() {

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
        return inflater.inflate(R.layout.logged_user_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSignOut.setOnClickListener {
            signOutUser()
        }
    }

    override fun onResume() {
        super.onResume()

        updateUI()
    }


    private fun updateUI() {
        val currentUser = auth.currentUser

        currentUser?.let {
            Log.d(Constants.LOG, "Logged")

            val userName = currentUser.displayName
            val email = currentUser.email

            userName?.let {
                TxtInputUsername.text = it.toEditable()
            }
            email?.let {
                txtInputEmail.text = it.toEditable()
            }

        } ?: let {
            Log.d(Constants.LOG, "not logged")

            signOutUser()
        }
    }

    private fun signOutUser() {
        auth.signOut()
        startActivity(Intent(activity, MainActivity::class.java))
    }

}