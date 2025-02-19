package com.example.fobutry

import android.app.ActivityOptions
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.bumptech.glide.Glide
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class HomePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homepage)

        val dbHelper = DatabaseHelper(this)
        val userId = intent.getLongExtra("USER_ID", -1L)

        if (userId != -1L) {
            val user = dbHelper.getUserDetails(userId)
            if (user != null) {
                val usernameTextView: TextView = findViewById(R.id.id_name)
                usernameTextView.text =
                    getString(R.string.welcome_message, user.firstName, user.lastName)

                val schoolnameTextView: TextView = findViewById(R.id.id_school_name)
                schoolnameTextView.text = getString(R.string.school_display, user.school)
            }

            // Load and display the profile picture
            val profilePic: ImageView = findViewById(R.id.Home_profilePic)

            val imageUri = dbHelper.getUserProfilePic(userId)
            if (!imageUri.isNullOrEmpty()) {
                Glide.with(this)
                    .load(Uri.parse(imageUri))
                    .placeholder(R.drawable.default_profile)
                    .error(R.drawable.default_profile)
                    .into(profilePic)
            }


            // Home button
            val homepageButton: ImageButton = findViewById(R.id.homebtn)
            homepageButton.setOnClickListener {
                val homes = Intent(this, HomePage::class.java)
                homes.putExtra("USER_ID", userId)
                startActivity(homes,  ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())

                finish()
            }

            // Schedule button
            val scheduleButton: MaterialButton = findViewById(R.id.button_schedule)
            scheduleButton.setOnClickListener {
                val schedule = Intent(this, ClassSchedule::class.java)
                schedule.putExtra("USER_ID", userId)
                startActivity(schedule,  ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
            }

            // Tasks button
            val taskButton: MaterialButton = findViewById(R.id.button_tasks)
            taskButton.setOnClickListener {
                val task = Intent(this, TasksActivity::class.java)
                task.putExtra("USER_ID", userId)
                startActivity(task,  ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
            }

            // Quiz button
            val quizButton: MaterialButton = findViewById(R.id.button_quizme)
            quizButton.setOnClickListener {
                val quiz = Intent(this, QuizActivity::class.java)
                quiz.putExtra("USER_ID", userId)
                startActivity(quiz,  ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
            }

            // Pomodoro button
            val pomodoroButton: ImageButton = findViewById(R.id.fcus)
            pomodoroButton.setOnClickListener {
                val pomo = Intent(this, focus::class.java)
                pomo.putExtra("USER_ID", userId)
                startActivity(pomo,  ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
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
        }
    }
}
