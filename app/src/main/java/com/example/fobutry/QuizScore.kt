package com.example.fobutry


import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class QuizScore : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.score_quiz)


        val textScoreNum: TextView = findViewById(R.id.text_Score_num) // Score display
        val backToQuizzes: Button = findViewById(R.id.back_to_quizzes)


        // Retrieve score and total number of questions from the intent extras.
        val score = intent.getIntExtra("SCORE", 0)
        val total = intent.getIntExtra("TOTAL", 0)


        // Display the score.
        textScoreNum.text = "$score/$total"




        // Set up the Back to Quizzes button.
        backToQuizzes.setOnClickListener {
            val quizListIntent = Intent(this, QuizActivity::class.java)
            quizListIntent.putExtra("USER_ID", intent.getLongExtra("USER_ID", -1L))
            startActivity(quizListIntent,  ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
            finish()
        }
    }
}
