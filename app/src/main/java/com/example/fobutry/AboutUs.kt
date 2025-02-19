package com.example.fobutry

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity


class AboutUs: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.aboutus)

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

        val backButton: ImageButton = findViewById(R.id.back)
        backButton.setOnClickListener {
            val button = Intent(this, Account::class.java)
            button.putExtra("USER_ID", userId)
            startActivity(button,  ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
        }
    }
}
