package com.example.fobutry

import android.app.ActivityOptions
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ClassSchedule : AppCompatActivity() {

    private lateinit var scheduleRecyclerView: RecyclerView
    private lateinit var scheduleAdapter: ScheduleAdapter
    private var userId: Long = -1
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.classschedule)

        dbHelper = DatabaseHelper(this)

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

        // Get userId
        userId = intent.getLongExtra("USER_ID", -1)

        if (userId == -1L) {
            Toast.makeText(this, "User ID not found!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        scheduleRecyclerView = findViewById(R.id.recyclerView_schedule)
        scheduleRecyclerView.layoutManager = LinearLayoutManager(this)

        // Adjust RecyclerView height dynamically
        val guideline = findViewById<androidx.constraintlayout.widget.Guideline>(R.id.guideline)
        scheduleRecyclerView.viewTreeObserver.addOnGlobalLayoutListener {
            val guidelineY = guideline.y // Get Y position of the guideline
            val recyclerViewY = scheduleRecyclerView.y // Get Y position of RecyclerView
            val maxHeight = guidelineY - recyclerViewY

            scheduleRecyclerView.layoutParams.height = maxHeight.toInt()
            scheduleRecyclerView.requestLayout()
        }

        loadSchedules()

        findViewById<ImageButton>(R.id.edit_schedulebtn).setOnClickListener {
            val bottomSheet = AddSchedule(userId) {
                loadSchedules() // Refresh schedules after adding
            }
            bottomSheet.show(supportFragmentManager, "AddSchedule")
        }
    }

    private fun loadSchedules() {
        val cursor: Cursor = dbHelper.getClassScheduleForUser(userId)
        val scheduleMap = mutableMapOf<String, MutableList<ScheduleDetail>>() // Group by weekday

        while (cursor.moveToNext()) {
            val subject = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCHEDULE_SUBJECT))
            val weekday = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCHEDULE_DAY))
            val time1 = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCHEDULE_TIME1))
            val time2 = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCHEDULE_TIME2))

            // Add schedule details under the correct weekday
            if (!scheduleMap.containsKey(weekday)) {
                scheduleMap[weekday] = mutableListOf()
            }
            scheduleMap[weekday]?.add(ScheduleDetail(subject, time1, time2))
        }
        cursor.close()

        // Convert map to a list of ScheduleModel
        val scheduleList = scheduleMap.map { (weekday, schedules) ->
            ScheduleModel(weekday, schedules)
        }

        scheduleAdapter = ScheduleAdapter(scheduleList.toMutableList(), dbHelper, userId)
        scheduleRecyclerView.adapter = scheduleAdapter
    }

}
