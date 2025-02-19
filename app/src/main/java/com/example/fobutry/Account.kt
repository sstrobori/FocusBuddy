package com.example.fobutry

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class Account : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account)

        val dbHelper = DatabaseHelper(this)


        val userId = intent.getLongExtra("USER_ID", -1L)

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



        // Logout Button
        val logoutButton: ImageButton = findViewById(R.id.logoutbtn)
        logoutButton.setOnClickListener {
            val logout = Intent(this, LoginActivity::class.java)
            startActivity(logout,  ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
        }


        if (userId != -1L) {
            val user = dbHelper.getUserDetails(userId)
            if (user != null) {
                val idnamesettingsEditText: TextView = findViewById(R.id.id_name_settings)
                idnamesettingsEditText.text = getString(R.string.namefrontsettings_display, user.firstName, user.lastName)
            }
        }

        // Account settings button
        val setAccountButton: MaterialButton = findViewById(R.id.button_account)
        setAccountButton.setOnClickListener {
            val setAccount = Intent(this, Account2::class.java)
            setAccount.putExtra("USER_ID", userId)
            startActivity(setAccount,  ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
        }

        // Profile settings button
        val setProfileButton: ImageButton = findViewById(R.id.setprofilebtn)
        setProfileButton.setOnClickListener {
            val setProfile = Intent(this, SettingsActivity::class.java)
            setProfile.putExtra("USER_ID", userId)
            startActivity(setProfile,  ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
        }

        // About Us button
        val setAboutButton: ImageButton = findViewById(R.id.setaboutbtn)
        setAboutButton.setOnClickListener {
            val aboutUs = Intent(this, AboutUs::class.java)
            aboutUs.putExtra("USER_ID", userId)
            startActivity(aboutUs,  ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
        }

    }
}
