package com.example.fobutry

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "focusbuddy.db"
        const val DATABASE_VERSION = 18

        const val TABLE_USERS = "users"
        const val TABLE_TASKS = "tasks"
        const val TABLE_SCHEDULES = "schedules"

        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_FIRST_NAME = "first_name"
        const val COLUMN_LAST_NAME = "last_name"
        const val COLUMN_SEX = "sex"
        const val COLUMN_CONTACT_NUMBER = "contact_number"
        const val COLUMN_SCHOOL = "school"
        const val COLUMN_CREATED_AT = "created_at"
        const val COLUMN_PROFILE_PIC = "profile_pic"

        // TASK
        const val COLUMN_TASK_ID = "task_id"
        const val COLUMN_TASK_TITLE = "task_title"
        const val COLUMN_IS_COMPLETED = "is_completed"

        // SCHEDULE
        const val COLUMN_SCHEDULE_ID = "schedule_id"
        const val COLUMN_SCHEDULE_SUBJECT = "subject"
        const val COLUMN_SCHEDULE_DAY = "day"
        const val COLUMN_SCHEDULE_TIME1 = "time1"
        const val COLUMN_SCHEDULE_TIME2 = "time2"


        //QUIZ
        const val TABLE_QUIZZES = "quizzes"
        const val COLUMN_QUIZ_ID = "quiz_id"
        const val COLUMN_QUIZ_TITLE = "quiz_title"
        const val COLUMN_QUIZ_USER_ID = "quiz_user_id"
        const val TABLE_QUESTIONS = "questions"
        const val COLUMN_QUESTION_ID = "question_id"
        const val COLUMN_QUESTION_TEXT = "question_text"
        const val COLUMN_OPTION_1 = "option_1"
        const val COLUMN_OPTION_2 = "option_2"
        const val COLUMN_OPTION_3 = "option_3"
        const val COLUMN_OPTION_4 = "option_4"
        const val COLUMN_CORRECT_OPTION = "correct_option"
        const val COLUMN_QUESTION_QUIZ_ID = "question_quiz_id"


        const val TABLE_USER_PROGRESS = "user_progress"
        const val COLUMN_PROGRESS_ID = "progress_id"
        const val COLUMN_PROGRESS_USER_ID = "progress_user_id"
        const val COLUMN_PROGRESS_QUIZ_ID = "progress_quiz_id"
        const val COLUMN_SCORE = "score"
        const val COLUMN_TOTAL_QUESTIONS = "total_questions"




    }

    override fun onCreate(db: SQLiteDatabase) {
        try {
            val createUsersTable = """
                CREATE TABLE $TABLE_USERS (
                    $COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $COLUMN_USERNAME TEXT UNIQUE NOT NULL,
                    $COLUMN_EMAIL TEXT UNIQUE NOT NULL,
                    $COLUMN_PASSWORD TEXT NOT NULL,
                    $COLUMN_FIRST_NAME TEXT NOT NULL,
                    $COLUMN_LAST_NAME TEXT NOT NULL,
                    $COLUMN_SEX TEXT CHECK($COLUMN_SEX IN ('Male', 'Female')) NOT NULL,
                    $COLUMN_CONTACT_NUMBER TEXT,
                    $COLUMN_SCHOOL TEXT,
                    $COLUMN_PROFILE_PIC TEXT,  -- ADD THIS COLUMN
                    $COLUMN_CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """.trimIndent()

            db.execSQL(createUsersTable)

            val createTasksTable = """
            CREATE TABLE $TABLE_TASKS (
                $COLUMN_TASK_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TASK_TITLE TEXT NOT NULL,
                $COLUMN_IS_COMPLETED INTEGER,
                $COLUMN_USER_ID INTEGER NOT NULL,
                FOREIGN KEY($COLUMN_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID) ON DELETE CASCADE
            )
        """.trimIndent()
            db.execSQL(createTasksTable)

            val createSchedulesTable = """
            CREATE TABLE $TABLE_SCHEDULES (
                $COLUMN_SCHEDULE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_SCHEDULE_SUBJECT TEXT NOT NULL,
                $COLUMN_SCHEDULE_DAY TEXT NOT NULL,
                $COLUMN_SCHEDULE_TIME1 TEXT NOT NULL,
                $COLUMN_SCHEDULE_TIME2 TEXT NOT NULL,
                $COLUMN_USER_ID INTEGER NOT NULL,
                FOREIGN KEY($COLUMN_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID) ON DELETE CASCADE
            )
        """.trimIndent()
            db.execSQL(createSchedulesTable)

            val createQuizzesTable = """
   CREATE TABLE $TABLE_QUIZZES (
      $COLUMN_QUIZ_ID INTEGER PRIMARY KEY AUTOINCREMENT,
      $COLUMN_QUIZ_TITLE TEXT NOT NULL,
      $COLUMN_QUIZ_USER_ID INTEGER,
      FOREIGN KEY($COLUMN_QUIZ_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID) ON DELETE CASCADE
   )
""".trimIndent()
            db.execSQL(createQuizzesTable)


            // QuizMe: Questions Table
            val createQuestionsTable = """
       CREATE TABLE $TABLE_QUESTIONS (
          $COLUMN_QUESTION_ID INTEGER PRIMARY KEY AUTOINCREMENT,
          $COLUMN_QUESTION_TEXT TEXT NOT NULL,
          $COLUMN_OPTION_1 TEXT NOT NULL,
          $COLUMN_OPTION_2 TEXT NOT NULL,
          $COLUMN_OPTION_3 TEXT NOT NULL,
          $COLUMN_OPTION_4 TEXT NOT NULL,
          $COLUMN_CORRECT_OPTION INTEGER NOT NULL,
          $COLUMN_QUESTION_QUIZ_ID INTEGER,
          FOREIGN KEY($COLUMN_QUESTION_QUIZ_ID) REFERENCES $TABLE_QUIZZES($COLUMN_QUIZ_ID) ON DELETE CASCADE
       )
   """.trimIndent()
            db.execSQL(createQuestionsTable)


            // QuizMe: User Progress Table
            val createUserProgressTable = """
       CREATE TABLE $TABLE_USER_PROGRESS (
          $COLUMN_PROGRESS_ID INTEGER PRIMARY KEY AUTOINCREMENT,
          $COLUMN_PROGRESS_USER_ID INTEGER,
          $COLUMN_PROGRESS_QUIZ_ID INTEGER,
          $COLUMN_SCORE INTEGER NOT NULL,
          $COLUMN_TOTAL_QUESTIONS INTEGER NOT NULL,
          FOREIGN KEY($COLUMN_PROGRESS_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID) ON DELETE CASCADE,
          FOREIGN KEY($COLUMN_PROGRESS_QUIZ_ID) REFERENCES $TABLE_QUIZZES($COLUMN_QUIZ_ID) ON DELETE CASCADE
       )
   """.trimIndent()
            db.execSQL(createUserProgressTable)



            android.util.Log.d("DatabaseHelper", "Tables created successfully!")

        } catch (e: Exception) {
            android.util.Log.e("DatabaseHelper", "Error creating tables: ${e.message}")
        }


    }




    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 18) { // Adjust version if needed
            db.execSQL("ALTER TABLE $TABLE_SCHEDULES ADD COLUMN $COLUMN_SCHEDULE_TIME1 TEXT")
            db.execSQL("ALTER TABLE $TABLE_SCHEDULES ADD COLUMN $COLUMN_SCHEDULE_TIME2 TEXT")
        }
    }




    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        db.execSQL("PRAGMA foreign_keys=ON;")
    }




    //PROFILE PICTURE

    fun updateProfilePicture(userId: Long, profilePicPath: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PROFILE_PIC, profilePicPath)
        }
        val result =
            db.update(TABLE_USERS, values, "$COLUMN_USER_ID = ?", arrayOf(userId.toString()))
        db.close()
        return result > 0
    }


    fun getUserProfilePic(userId: Long): String? {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COLUMN_PROFILE_PIC FROM $TABLE_USERS WHERE $COLUMN_USER_ID = ?",
            arrayOf(userId.toString())
        )
        var imageUri: String? = null
        if (cursor.moveToFirst()) {
            imageUri = cursor.getString(0)
        }
        cursor.close()
        db.close()
        return imageUri
    }


    // Register a new user
    fun registerUser(
        username: String,
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        sex: String,
        contactNumber: String,
        school: String
    ): Boolean {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_EMAIL, email)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_FIRST_NAME, firstName)
            put(COLUMN_LAST_NAME, lastName)
            put(COLUMN_SEX, sex)
            put(COLUMN_CONTACT_NUMBER, contactNumber)
            put(COLUMN_SCHOOL, school)
        }
        return db.insert(TABLE_USERS, null, contentValues) != -1L
    }


    fun checkUserLogin(usernameOrEmail: String, password: String): Boolean {
        val db = readableDatabase
        val query =
            "SELECT * FROM $TABLE_USERS WHERE ($COLUMN_USERNAME = ? OR $COLUMN_EMAIL = ?) AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(usernameOrEmail, usernameOrEmail, password))
        val loginSuccess = cursor.count > 0
        cursor.close()
        return loginSuccess
    }

    fun getUserId(usernameOrEmail: String): Long {
        val db = readableDatabase
        val query =
            "SELECT $COLUMN_USER_ID FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ? OR $COLUMN_EMAIL = ?"
        val cursor = db.rawQuery(query, arrayOf(usernameOrEmail, usernameOrEmail))
        return if (cursor.moveToFirst()) {
            val userId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_USER_ID))
            cursor.close()
            userId
        } else {
            cursor.close()
            -1L
        }
    }

    //GET USER DETAILS IN SETTINGS

    fun getUserDetails(userId: Long): User? {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USER_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))

        return if (cursor.moveToFirst()) {
            val user = User(
                userId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),
                username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                firstName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME)),
                lastName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)),
                middleInitial = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)),
                sex = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SEX)),
                contactNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTACT_NUMBER)),
                school = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SCHOOL))
            )
            cursor.close()
            user
        } else {
            cursor.close()
            null
        }
    }

    //UPDATE USER DATA IN SETTINGS

    fun updateUser(
        userId: Long,
        firstName: String,
        lastName: String,
        school: String,
        contactNumber: String
    ): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_FIRST_NAME, firstName)
            put(COLUMN_LAST_NAME, lastName)
            put(COLUMN_SCHOOL, school)
            put(COLUMN_CONTACT_NUMBER, contactNumber)
        }

        val result =
            db.update(TABLE_USERS, values, "$COLUMN_USER_ID = ?", arrayOf(userId.toString()))
        db.close()
        return result > 0
    }

    fun updateAccount(userId: Long, username: String, email: String, password: String?): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_EMAIL, email)
            if (!password.isNullOrEmpty()) {
                put(COLUMN_PASSWORD, password) // Update password only if entered
            }
        }

        val result =
            db.update(TABLE_USERS, values, "$COLUMN_USER_ID = ?", arrayOf(userId.toString()))
        db.close()
        return result > 0
    }


    //-----CLASS SCHEDULE

    fun addClassSchedule(subject: String, day: String, time1: String,time2: String, userId: Long): Boolean {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_SCHEDULE_SUBJECT, subject)
            put(COLUMN_SCHEDULE_DAY, day)
            put(COLUMN_SCHEDULE_TIME1, time1)
            put(COLUMN_SCHEDULE_TIME2, time2)
            put(COLUMN_USER_ID, userId)
        }
        return db.insert(TABLE_SCHEDULES, null, contentValues) != -1L // Converts Long to Boolean
    }




    fun getClassScheduleForUser(userId: Long): Cursor {
        val db = readableDatabase
        val query = """
            SELECT $COLUMN_SCHEDULE_SUBJECT, $COLUMN_SCHEDULE_DAY, $COLUMN_SCHEDULE_TIME1 , $COLUMN_SCHEDULE_TIME2
            FROM $TABLE_SCHEDULES 
            WHERE $COLUMN_USER_ID = ? 
            ORDER BY 
                CASE 
                    WHEN $COLUMN_SCHEDULE_DAY = 'Monday' THEN 1
                    WHEN $COLUMN_SCHEDULE_DAY = 'Tuesday' THEN 2
                    WHEN $COLUMN_SCHEDULE_DAY = 'Wednesday' THEN 3
                    WHEN $COLUMN_SCHEDULE_DAY = 'Thursday' THEN 4
                    WHEN $COLUMN_SCHEDULE_DAY = 'Friday' THEN 5
                    WHEN $COLUMN_SCHEDULE_DAY = 'Saturday' THEN 6
                    WHEN $COLUMN_SCHEDULE_DAY = 'Sunday' THEN 7
                    ELSE 8 
                END,
                $COLUMN_SCHEDULE_TIME1 ASC
        """.trimIndent()
        return db.rawQuery(query, arrayOf(userId.toString()))
    }


    fun deleteSchedule(subject: String, day: String, time1: String, time2: String, userId: Long): Boolean {
        val db = writableDatabase
        val result = db.delete(
            TABLE_SCHEDULES,
            "$COLUMN_SCHEDULE_SUBJECT = ? AND $COLUMN_SCHEDULE_DAY = ? AND $COLUMN_SCHEDULE_TIME1 = ? AND $COLUMN_SCHEDULE_TIME2 = ? AND $COLUMN_USER_ID = ?",
            arrayOf(subject, day, time1, time2, userId.toString())
        )
        db.close()
        return result > 0
    }

    //-----TASK

    fun addTask(taskTitle: String, isCompleted: Boolean, userId: Long): Boolean {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_TASK_TITLE, taskTitle)
            put(
                COLUMN_IS_COMPLETED,
                if (isCompleted) 1 else 0
            ) // Store Boolean as 1 (true) or 0 (false)
            put(COLUMN_USER_ID, userId)
        }
        return db.insert(TABLE_TASKS, null, contentValues) != -1L
    }


    fun getTasksForUser(userId: Long): Cursor {
        val db = readableDatabase
        return db.rawQuery(
            "SELECT $COLUMN_TASK_TITLE, $COLUMN_IS_COMPLETED FROM $TABLE_TASKS WHERE $COLUMN_USER_ID = ?",
            arrayOf(userId.toString())
        )
    }

    fun updateTaskCompletion(taskTitle: String, isCompleted: Boolean, userId: Long): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_IS_COMPLETED, if (isCompleted) 1 else 0)
        }
        val rowsUpdated = db.update(
            TABLE_TASKS, values,
            "$COLUMN_TASK_TITLE = ? AND $COLUMN_USER_ID = ?",
            arrayOf(taskTitle, userId.toString())
        )
        db.close()
        return rowsUpdated > 0
    }

    fun deleteTask(taskTitle: String, isCompleted: Boolean, userId: Long): Boolean {
        val db = writableDatabase
        val result = db.delete(
            TABLE_TASKS,
            "$COLUMN_TASK_TITLE = ? AND $COLUMN_IS_COMPLETED = ? AND $COLUMN_USER_ID = ?",
            arrayOf(taskTitle, if (isCompleted) "1" else "0", userId.toString())
        )
        db.close()
        return result > 0
    }

    //QUIZZES


    fun insertQuizWithQuestions(
        userId: Long,
        quizTitle: String,
        questions: List<Question>
    ): Boolean {
        val db = writableDatabase
        db.beginTransaction()
        try {
            // Insert quiz into TABLE_QUIZZES.
            val quizValues = ContentValues().apply {
                put(COLUMN_QUIZ_USER_ID, userId)
                put(
                    COLUMN_QUIZ_TITLE,
                    quizTitle
                ) // Make sure your constant matches your table definition.
            }
            val quizId = db.insert(TABLE_QUIZZES, null, quizValues)
            if (quizId == -1L) return false


            // Insert each question into TABLE_QUESTIONS.
            for (q in questions) {
                val questionValues = ContentValues().apply {
                    put(COLUMN_QUESTION_TEXT, q.questionText)
                    put(COLUMN_OPTION_1, q.options[0])
                    put(COLUMN_OPTION_2, q.options[1])
                    put(COLUMN_OPTION_3, q.options[2])
                    put(COLUMN_OPTION_4, q.options[3])
                    put(COLUMN_CORRECT_OPTION, q.correctOption)
                    put(COLUMN_QUESTION_QUIZ_ID, quizId)
                }
                val questionId = db.insert(TABLE_QUESTIONS, null, questionValues)
                if (questionId == -1L) return false
            }


            db.setTransactionSuccessful()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    fun getAllQuizzesForUser(userId: Long): List<Quiz> {
        val quizList = mutableListOf<Quiz>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_QUIZZES, null, "$COLUMN_QUIZ_USER_ID = ?",
            arrayOf(userId.toString()), null, null, null
        )
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_QUIZ_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUIZ_TITLE))
                quizList.add(Quiz(id, userId, title))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return quizList
    }




    fun getQuestionsForQuiz(quizId: Long): List<Question> {
        val questionList = mutableListOf<Question>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_QUESTIONS, null, "$COLUMN_QUESTION_QUIZ_ID = ?",
            arrayOf(quizId.toString()), null, null, null
        )
        if (cursor.moveToFirst()) {
            do {
                val questionText = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUESTION_TEXT))
                val options = listOf(
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION_1)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION_2)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION_3)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION_4))
                )
                val correctOption = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CORRECT_OPTION))
                questionList.add(Question(questionText, options, correctOption))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return questionList
    }




    fun deleteQuiz(quizId: Long) {
        val db = this.writableDatabase
        try {
            db.beginTransaction()

            // Delete all questions related to the quiz first
            db.delete(TABLE_QUESTIONS, "$COLUMN_QUESTION_QUIZ_ID = ?", arrayOf(quizId.toString()))

            // Now delete the quiz itself
            val deletedRows = db.delete(TABLE_QUIZZES, "$COLUMN_QUIZ_ID = ?", arrayOf(quizId.toString()))

            if (deletedRows > 0) {
                db.setTransactionSuccessful()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
            db.close()
        }
    }







}


