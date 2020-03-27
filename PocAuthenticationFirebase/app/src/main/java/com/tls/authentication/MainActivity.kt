package com.tls.authentication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.tls.authentication.login.LoggedUserActivity
import com.tls.authentication.login.SignInActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()

        if (auth.currentUser == null) startActivity(
            Intent(
                this@MainActivity,
                SignInActivity::class.java
            )
        )
        else startActivity(Intent(this@MainActivity, LoggedUserActivity::class.java))
    }


}
