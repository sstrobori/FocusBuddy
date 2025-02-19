package com.example.fobutry

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class LoginActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        dbHelper = DatabaseHelper(this)

        val usernameLoginEditText: EditText = findViewById(R.id.etUsername)
        val passwordLoginEditText: EditText = findViewById(R.id.etPassword)
        val loginButton: MaterialButton = findViewById(R.id.btnLogin)
        val tvCreateAccount: TextView = findViewById(R.id.tvCreateAccount)

        // Redirect to CreateAccount Activity when tvCreateAccount is clicked
        tvCreateAccount.setOnClickListener {
            redirectToCreateAccount()
        }

        loginButton.setOnClickListener {
            val usernameOrEmail = usernameLoginEditText.text.toString()
            val password = passwordLoginEditText.text.toString()

            if (dbHelper.checkUserLogin(usernameOrEmail, password)) {

                val userId = dbHelper.getUserId(usernameOrEmail)

                // Login successful, proceed to homepage
                if(userId != -1L) {
                    Toast.makeText(this, "Welcome to FocusBuddy", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomePage::class.java)
                    intent.putExtra("USER_ID", userId)  // Pass the user ID
                    startActivity(intent)
                    finish()
                }
            } else {
                // Show error message for incorrect login
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to redirect to CreateAccount activity
    private fun redirectToCreateAccount() {
        val createAccountIntent = Intent(this, CreateAccount::class.java)
        startActivity(createAccountIntent,  ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
    }
}
