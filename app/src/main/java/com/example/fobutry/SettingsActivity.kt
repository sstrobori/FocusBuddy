package com.example.fobutry

import android.app.ActivityOptions
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    private var imageURI: Uri? = null

    private lateinit var imageView: ImageView
    private lateinit var btnImagePicker: Button

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var firstNameField: EditText
    private lateinit var lastNameField: EditText
    private lateinit var schoolField: EditText
    private lateinit var contactNumberField: EditText

    // New Activity Result Launcher for image picking
// Inside the image picker result launcher
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageURI = it
            imageView.setImageURI(it)

            // Save the image URI in the database
            val userId = intent.getLongExtra("USER_ID", -1L)
            if (userId != -1L) {
                Log.d("SettingsActivity", "Saving profile picture URI")
                val success = databaseHelper.updateProfilePicture(userId, it.toString())
                if (success) {
                    Toast.makeText(this, "Profile picture updated!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to update profile picture!", Toast.LENGTH_SHORT).show()
                }
            }


        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)

        imageView = findViewById(R.id.pick_profilePic)
        btnImagePicker = findViewById(R.id.pick_profilebtn)

        btnImagePicker.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        databaseHelper = DatabaseHelper(this)

        firstNameField = findViewById(R.id.editText_Fname)
        lastNameField = findViewById(R.id.editText_Lname)
        schoolField = findViewById(R.id.editText_school)
        contactNumberField = findViewById(R.id.editText_number)

        val userId = intent.getLongExtra("USER_ID", -1L)

        setupNavigationButtons(userId)

        if (userId != -1L) {
            loadUserData()
        }

        // Save profile button
        val saveButton: Button = findViewById(R.id.button_save_profile)
        saveButton.setOnClickListener {
            updateUserData()
        }
    }

    private fun setupNavigationButtons(userId: Long) {
        val homepageButton: ImageButton = findViewById(R.id.homebtn)
        homepageButton.setOnClickListener {
            val intent = Intent(this, HomePage::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent,  ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
        }

        val settingsButton: ImageButton = findViewById(R.id.settingsbtn)
        settingsButton.setOnClickListener {
            val intent = Intent(this, Account::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent,  ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
        }

        val profileButton: ImageButton = findViewById(R.id.profilebtn)
        profileButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent,  ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
        }

        val backButton: ImageButton = findViewById(R.id.back)
        backButton.setOnClickListener {
            val intent = Intent(this, Account::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent,  ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
        }
    }

    private fun loadUserData() {
        val userId = intent.getLongExtra("USER_ID", -1L)
        val user = databaseHelper.getUserDetails(userId)
        if (user != null) {
            firstNameField.hint = user.firstName
            lastNameField.hint = user.lastName
            schoolField.hint = user.school
            contactNumberField.hint = user.contactNumber
        }
    }

    private fun updateUserData() {
        val userId = intent.getLongExtra("USER_ID", -1L)
        val newFirstName = firstNameField.text.toString().trim().ifEmpty { firstNameField.hint.toString() }
        val newLastName = lastNameField.text.toString().trim().ifEmpty { lastNameField.hint.toString() }
        val newSchool = schoolField.text.toString().trim().ifEmpty { schoolField.hint.toString() }
        val newContactNumber = contactNumberField.text.toString().trim().ifEmpty { contactNumberField.hint.toString() }

        val success = databaseHelper.updateUser(
            userId, newFirstName, newLastName, newSchool, newContactNumber
        )

        if (success) {
            Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
            loadUserData()
        } else {
            Toast.makeText(this, "Failed to update profile.", Toast.LENGTH_SHORT).show()
        }
    }
}
