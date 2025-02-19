package com.example.fobutry

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class CreateAccount : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_account)

        dbHelper = DatabaseHelper(this)

        val usernameEditText: EditText = findViewById(R.id.etUsername)
        val emailEditText: EditText = findViewById(R.id.etEmail)
        val passwordEditText: EditText = findViewById(R.id.etPassword)
        val confirmPasswordEditText: EditText = findViewById(R.id.etConfirmPassword)
        val firstNameEditText: EditText = findViewById(R.id.Fname)
        val lastNameEditText: EditText = findViewById(R.id.Lname)
        val contactNumberEditText: EditText = findViewById(R.id.ContactNumber)
        val schoolEditText: EditText = findViewById(R.id.School)
        val sexRadioGroup: RadioGroup = findViewById(R.id.radioGroupGender)

        val nextRegisterButton: MaterialButton = findViewById(R.id.next_signupbtn)
        val tvLoginbtn: TextView = findViewById(R.id.tvLoginbtn) // Add TextView for login button

        tvLoginbtn.setOnClickListener {
            redirectToLoginActivity()
        }

        nextRegisterButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()
            val firstName = firstNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()
            val contactNumber = contactNumberEditText.text.toString()
            val school = schoolEditText.text.toString()
            val selectedSexId = sexRadioGroup.checkedRadioButtonId

            val selectedSexButton: RadioButton? = findViewById(selectedSexId)
            val sex = selectedSexButton?.text.toString()

            if (password == confirmPassword) {
                val result = dbHelper.registerUser(username, email, password, firstName, lastName, sex, contactNumber, school)
                if (result) {
                    // 🔹 Retrieve the user ID after successful registration
                    val userId = dbHelper.getUserId(username)

                    if (userId != -1L) {
                        // 🔹 Store user ID in SharedPreferences
                        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putLong("user_id", userId)
                        editor.apply()

                        Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()

                        // 🔹 Redirect to LoginActivity
                        val loginIntent = Intent(this, LoginActivity::class.java)
                        startActivity(loginIntent,  ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
                        finish()
                    } else {
                        Toast.makeText(this, "Failed to retrieve user ID", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
                    Log.e("CreateAccount", "User registration failed for username: $username")
                }
            } else {
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    // Function to redirect to LoginActivity
    private fun redirectToLoginActivity() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent,  ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
        finish() // Optionally close the current activity to prevent going back to registration screen
    }
}
