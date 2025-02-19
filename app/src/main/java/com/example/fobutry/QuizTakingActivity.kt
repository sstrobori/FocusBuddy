package com.example.fobutry


import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class QuizTakingActivity : AppCompatActivity() {


    private lateinit var dbHelper: DatabaseHelper
    private var userId: Long = -1L
    private var quizId: Long = -1L


    private lateinit var questionTextView: TextView
    private lateinit var optionsRadioGroup: RadioGroup
    private lateinit var option1: RadioButton
    private lateinit var option2: RadioButton
    private lateinit var option3: RadioButton
    private lateinit var option4: RadioButton
    private lateinit var nextButton: Button


    private lateinit var questions: List<Question>
    private var currentQuestionIndex: Int = 0
    private var score: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_quiz)


        dbHelper = DatabaseHelper(this)


        // Retrieve USER_ID and QUIZ_ID from the intent extras.
        userId = intent.getLongExtra("USER_ID", -1L)
        quizId = intent.getLongExtra("QUIZ_ID", -1L)
        Log.d("QuizTakingActivity", "Received USER_ID: $userId, QUIZ_ID: $quizId")
        if (userId == -1L || quizId == -1L) {
            Toast.makeText(this, "Invalid user or quiz", Toast.LENGTH_SHORT).show()
            finish()
            return
        }


        // Bind UI elements.
        questionTextView = findViewById(R.id.question_text)
        optionsRadioGroup = findViewById(R.id.answer_choices_group)
        option1 = findViewById(R.id.answer_1)
        option2 = findViewById(R.id.answer_2)
        option3 = findViewById(R.id.answer_3)
        option4 = findViewById(R.id.answer_4)
        nextButton = findViewById(R.id.next_button)


        // Load questions from the database.
        questions = dbHelper.getQuestionsForQuiz(quizId)
        if (questions.isEmpty()) {
            Toast.makeText(this, "No questions found for this quiz", Toast.LENGTH_SHORT).show()
            finish()
            return
        }


        displayQuestion(currentQuestionIndex)


        nextButton.setOnClickListener {
            // Check that an answer is selected.
            val selectedId = optionsRadioGroup.checkedRadioButtonId
            if (selectedId == -1) {
                Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            // Determine the selected option number.
            val selectedOption = when (selectedId) {
                option1.id -> 1
                option2.id -> 2
                option3.id -> 3
                option4.id -> 4
                else -> 0
            }


            val currentQuestion = questions[currentQuestionIndex]
            if (selectedOption == currentQuestion.correctOption) {
                score++
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Incorrect", Toast.LENGTH_SHORT).show()
            }


            // Move to the next question.
            currentQuestionIndex++
            if (currentQuestionIndex < questions.size) {
                displayQuestion(currentQuestionIndex)
            } else {
                // Quiz finished: start the QuizScoreActivity
                val scoreIntent = Intent(this, QuizScore::class.java)
                scoreIntent.putExtra("SCORE", score)
                scoreIntent.putExtra("TOTAL", questions.size)
                scoreIntent.putExtra("USER_ID", userId)
                startActivity(scoreIntent,  ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
                finish()
            }


        }
    }


    private fun displayQuestion(index: Int) {
        val question = questions[index]
        questionTextView.text = question.questionText


        // Update the text for each RadioButton.
        option1.text = question.options[0]
        option2.text = question.options[1]
        option3.text = question.options[2]
        option4.text = question.options[3]


        // Clear any previous selection.
        optionsRadioGroup.clearCheck()
    }
}
