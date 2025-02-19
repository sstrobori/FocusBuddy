package com.example.fobutry

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class QuizInput : AppCompatActivity() {


    private lateinit var dbHelper: DatabaseHelper
    private var userId: Long = -1L


    // UI elements for the first question
    private lateinit var quizTitle: EditText
    private lateinit var quizQuestion: EditText
    private lateinit var choice1: EditText
    private lateinit var choice2: EditText
    private lateinit var choice3: EditText
    private lateinit var choice4: EditText
    private lateinit var rAns1: RadioButton
    private lateinit var rAns2: RadioButton
    private lateinit var rAns3: RadioButton
    private lateinit var rAns4: RadioButton


    // Container for additional questions
    private lateinit var additionalQuestionsContainer: LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.createquiz)


        // Retrieve the user ID from the intent extras.
        userId = intent.getLongExtra("USER_ID", -1L)
        if (userId == -1L) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            finish()
            return
        }


        dbHelper = DatabaseHelper(this)


        // Bind the UI elements for the first question.
        quizTitle = findViewById(R.id.quiz_title)
        quizQuestion = findViewById(R.id.quiz_question)
        choice1 = findViewById(R.id.answer_choice_1)
        choice2 = findViewById(R.id.answer_choice_2)
        choice3 = findViewById(R.id.answer_choice_3)
        choice4 = findViewById(R.id.answer_choice_4)
        rAns1 = findViewById(R.id.right_answer_1)
        rAns2 = findViewById(R.id.right_answer_2)
        rAns3 = findViewById(R.id.right_answer_3)
        rAns4 = findViewById(R.id.right_answer_4)


        // Get the container for additional questions; this must be defined in createquiz.xml.
        additionalQuestionsContainer = findViewById(R.id.additional_questions_container)


        // Handle "Add another question" click.
        val addQuestion: TextView = findViewById(R.id.add_question)
        addQuestion.setOnClickListener {
            // Inflate additional question layout (create a layout resource: additional_question.xml).
            val inflater = LayoutInflater.from(this)
            val additionalView = inflater.inflate(R.layout.additional_question, additionalQuestionsContainer, false)
            additionalQuestionsContainer.addView(additionalView)
        }


        val saveQuizBtn: Button = findViewById(R.id.save_quiz_button)
        saveQuizBtn.setOnClickListener {
            // Validate and collect data for the quiz.
            val title = quizTitle.text.toString().trim()
            if (title.isEmpty()) {
                Toast.makeText(this, "Quiz title cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            // Validate and collect the first question.
            val firstQuestionText = quizQuestion.text.toString().trim()
            val firstOpt1 = choice1.text.toString().trim()
            val firstOpt2 = choice2.text.toString().trim()
            val firstOpt3 = choice3.text.toString().trim()
            val firstOpt4 = choice4.text.toString().trim()
            if (firstQuestionText.isEmpty() || firstOpt1.isEmpty() || firstOpt2.isEmpty() ||
                firstOpt3.isEmpty() || firstOpt4.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields for the first question", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val firstCorrectOption = when {
                rAns1.isChecked -> 1
                rAns2.isChecked -> 2
                rAns3.isChecked -> 3
                rAns4.isChecked -> 4
                else -> {
                    Toast.makeText(this, "Please select the correct answer for the first question", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }


            // Create a list to hold all questions.
            val questions = mutableListOf<Question>()
            // Add the first question.
            questions.add(
                Question(
                    questionText = firstQuestionText,
                    options = listOf(firstOpt1, firstOpt2, firstOpt3, firstOpt4),
                    correctOption = firstCorrectOption
                )
            )


            // Loop over all additional question views in the container.
            for (i in 0 until additionalQuestionsContainer.childCount) {
                val additionalView = additionalQuestionsContainer.getChildAt(i)
                // In additional_question.xml, define the following IDs:
                // additional_question_text, additional_choice_1, additional_choice_2, additional_choice_3, additional_choice_4,
                // additional_radio_group (containing additional_right_answer_1, additional_right_answer_2, additional_right_answer_3, additional_right_answer_4)
                val addQuestionText = additionalView.findViewById<EditText>(R.id.additional_question_text)?.text.toString().trim()
                val addChoice1 = additionalView.findViewById<EditText>(R.id.additional_choice_1)?.text.toString().trim()
                val addChoice2 = additionalView.findViewById<EditText>(R.id.additional_choice_2)?.text.toString().trim()
                val addChoice3 = additionalView.findViewById<EditText>(R.id.additional_choice_3)?.text.toString().trim()
                val addChoice4 = additionalView.findViewById<EditText>(R.id.additional_choice_4)?.text.toString().trim()


                val additionalRadioGroup = additionalView.findViewById<RadioGroup>(R.id.additional_radio_group)
                val selectedId = additionalRadioGroup.checkedRadioButtonId
                val additionalCorrectOption = when (selectedId) {
                    additionalView.findViewById<RadioButton>(R.id.additional_right_answer_1).id -> 1
                    additionalView.findViewById<RadioButton>(R.id.additional_right_answer_2).id -> 2
                    additionalView.findViewById<RadioButton>(R.id.additional_right_answer_3).id -> 3
                    additionalView.findViewById<RadioButton>(R.id.additional_right_answer_4).id -> 4
                    else -> {
                        Toast.makeText(this, "Please select the correct answer for an additional question", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                }


                // Validate that fields are not empty.
                if (addQuestionText.isEmpty() || addChoice1.isEmpty() || addChoice2.isEmpty() ||
                    addChoice3.isEmpty() || addChoice4.isEmpty()) {
                    Toast.makeText(this, "Please fill in all fields for an additional question", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }


                questions.add(
                    Question(
                        questionText = addQuestionText,
                        options = listOf(addChoice1, addChoice2, addChoice3, addChoice4),
                        correctOption = additionalCorrectOption
                    )
                )
            }


            // Now insert the quiz along with all its questions into the database.
            val success = dbHelper.insertQuizWithQuestions(userId, title, questions)
            if (success) {
                Toast.makeText(this, "Quiz created successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, QuizActivity::class.java)
                intent.putExtra("USER_ID", userId)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Failed to create quiz", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
