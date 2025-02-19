package com.example.fobutry


import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class QuizActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private var userId: Long = -1L
    private lateinit var quizRecyclerView: RecyclerView
    private lateinit var quizAdapter: QuizAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quizme)

        dbHelper = DatabaseHelper(this)
        userId = intent.getLongExtra("USER_ID", -1L)

        if (userId == -1L) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        quizRecyclerView = findViewById(R.id.recyclerView_CreatedQuiz)
        quizRecyclerView.layoutManager = LinearLayoutManager(this)

        // Pass delete function to adapter
        quizAdapter = QuizAdapter { quiz ->
            deleteQuiz(quiz.id)
        }.apply {
            onItemClick = { quiz ->
                val intent = Intent(this@QuizActivity, QuizTakingActivity::class.java)
                intent.putExtra("USER_ID", userId)
                intent.putExtra("QUIZ_ID", quiz.id)
                startActivity(intent, ActivityOptions.makeCustomAnimation(this@QuizActivity, 0, 0).toBundle())
            }
        }

        quizRecyclerView.adapter = quizAdapter
        loadQuizzesForUser()

        // Set up buttons
        findViewById<ImageButton>(R.id.edit_quizmebtn).setOnClickListener {
            val createIntent = Intent(this, QuizInput::class.java)
            createIntent.putExtra("USER_ID", userId)
            startActivity(createIntent, ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
        }

        findViewById<ImageButton>(R.id.homebtn).setOnClickListener {
            val homes = Intent(this, HomePage::class.java)
            homes.putExtra("USER_ID", userId)
            startActivity(homes, ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
            finish()
        }

        findViewById<ImageButton>(R.id.settingsbtn).setOnClickListener {
            val account = Intent(this, Account::class.java)
            account.putExtra("USER_ID", userId)
            startActivity(account, ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
        }

        findViewById<ImageButton>(R.id.profilebtn).setOnClickListener {
            val profile = Intent(this, SettingsActivity::class.java)
            profile.putExtra("USER_ID", userId)
            startActivity(profile, ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
        }
    }

    private fun loadQuizzesForUser() {
        try {
            val quizzes = dbHelper.getAllQuizzesForUser(userId)
            quizAdapter.submitList(quizzes)
        } catch (e: Exception) {
            Log.e("QuizActivity", "Error loading quizzes", e)
            Toast.makeText(this, "Error loading quizzes", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteQuiz(quizId: Long) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Delete Quiz")
        builder.setMessage("Are you sure you want to delete this quiz?")
        builder.setPositiveButton("Yes") { _, _ ->
            dbHelper.deleteQuiz(quizId)
            Toast.makeText(this, "Quiz deleted", Toast.LENGTH_SHORT).show()
            loadQuizzesForUser()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

}
