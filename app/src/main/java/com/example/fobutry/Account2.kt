package com.example.fobutry

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Account2 : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var usernameSettingsField: EditText
    private lateinit var emailUsernameField: EditText
    private lateinit var passwordSettingsField: EditText
    private var userId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account2)

        databaseHelper = DatabaseHelper(this)

        usernameSettingsField = findViewById(R.id.edit_username)
        emailUsernameField = findViewById(R.id.editText_email)
        passwordSettingsField = findViewById(R.id.editText_password)

        userId = intent.getLongExtra("USER_ID", -1L)

        // Home button
        val homepageButton: ImageButton = findViewById(R.id.homebtn)
        homepageButton.setOnClickListener {
            val homes = Intent(this, HomePage::class.java)
            homes.putExtra("USER_ID", userId)
            startActivity(homes,  ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
        }

        // Settings button
        val settingsButton: ImageButton = findViewById(R.id.settingsbtn)
        settingsButton.setOnClickListener {
            val account = Intent(this, Account::class.java)
            account.putExtra("USER_ID", userId)
            startActivity(account,  ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
        }

        // Profile button
        val profileButton: ImageButton = findViewById(R.id.profilebtn)
        profileButton.setOnClickListener {
            val profile = Intent(this, SettingsActivity::class.java)
            profile.putExtra("USER_ID", userId)
            startActivity(profile,  ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
        }

        val backButton: ImageButton = findViewById(R.id.back)
        backButton.setOnClickListener {
            val button = Intent(this, Account::class.java)
            button.putExtra("USER_ID", userId)
            startActivity(button,  ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
        }

        if (userId != -1L) {
            loadUserData()
        }

        // Save account button
        val saveButton: Button = findViewById(R.id.button_save_account)
        saveButton.setOnClickListener {
            updateAccountData()
        }
    }


    private fun loadUserData() {
        val user = databaseHelper.getUserDetails(userId)
        if (user != null) {
            usernameSettingsField.hint = user.username
            emailUsernameField.hint = user.email
            passwordSettingsField.hint = "********" // Mask password for security
        }
    }

    private fun updateAccountData() {
        val newUsername = usernameSettingsField.text.toString().trim()
            .ifEmpty { usernameSettingsField.hint.toString() }
        val newEmail =
            emailUsernameField.text.toString().trim().ifEmpty { emailUsernameField.hint.toString() }
        val newPassword =
            passwordSettingsField.text.toString().trim().ifEmpty { null } // Only update if entered

        val success = databaseHelper.updateAccount(userId, newUsername, newEmail, newPassword)

        if (success) {
            Toast.makeText(this, "Account updated successfully!", Toast.LENGTH_SHORT).show()
            loadUserData() // Refresh hints with updated values
        } else {
            Toast.makeText(this, "Failed to update account.", Toast.LENGTH_SHORT).show()
        }



    }
}
