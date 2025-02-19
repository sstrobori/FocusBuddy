package com.example.fobutry

import android.app.ActivityOptions
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TasksActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var taskRecyclerView: RecyclerView
    private var userId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.todo)

        dbHelper = DatabaseHelper(this)

        // Retrieve USER_ID
        userId = intent.getLongExtra("USER_ID", -1)
        Log.d("TasksActivity", "Received USER_ID: $userId")

        if (userId == -1L) {
            Toast.makeText(this, "User ID not found!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Initialize RecyclerView
        taskRecyclerView = findViewById(R.id.recyclerView)
        taskRecyclerView.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter(mutableListOf(), dbHelper, userId)
        taskRecyclerView.adapter = taskAdapter

        // Adjust RecyclerView height dynamically
        val createTaskBtn = findViewById<ImageButton>(R.id.create_taskBtn)
        taskRecyclerView.viewTreeObserver.addOnGlobalLayoutListener {
            val imageButtonY = createTaskBtn.y // Get Y position of ImageButton
            val recyclerViewY = taskRecyclerView.y  // Get Y position of RecyclerView
            val maxHeight = imageButtonY - recyclerViewY

            taskRecyclerView.layoutParams.height = maxHeight.toInt()
            taskRecyclerView.requestLayout()
        }

        loadTasks()

        // Home button
        findViewById<ImageButton>(R.id.homebtn).setOnClickListener {
            val homeIntent = Intent(this, HomePage::class.java)
            homeIntent.putExtra("USER_ID", userId)
            startActivity(homeIntent,  ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
        }

        // Settings button
        findViewById<ImageButton>(R.id.settingsbtn).setOnClickListener {
            val settingsIntent = Intent(this, Account::class.java)
            settingsIntent.putExtra("USER_ID", userId)
            startActivity(settingsIntent,  ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
        }

        // Profile button
        findViewById<ImageButton>(R.id.profilebtn).setOnClickListener {
            val profileIntent = Intent(this, SettingsActivity::class.java)
            profileIntent.putExtra("USER_ID", userId)
            startActivity(profileIntent,  ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
        }

        // Add new task button
        findViewById<ImageButton>(R.id.create_taskBtn).setOnClickListener {
            val bottomSheet = AddNewTaskActivity(userId) {
                loadTasks() // Refresh tasks after adding a new one
            }
            bottomSheet.show(supportFragmentManager, "AddNewTaskActivity")
        }
    }

    private fun loadTasks() {
        val cursor: Cursor = dbHelper.getTasksForUser(userId)

        if (cursor.count == 0) { // Check if cursor is empty
            Toast.makeText(this, "No tasks found.", Toast.LENGTH_SHORT).show()
            return
        }

        val taskList = mutableListOf<Task>()
        while (cursor.moveToNext()) {
            val taskTitle = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_TITLE))
            val isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IS_COMPLETED)) == 1
            taskList.add(Task(taskTitle, isCompleted))
        }
        cursor.close()

        taskAdapter.updateTasks(taskList)
    }
}
