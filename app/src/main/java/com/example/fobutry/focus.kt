package com.example.fobutry


import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class focus : AppCompatActivity() {

    private lateinit var timerTextView: TextView
    private var startButton: Button? = null
    private var endButton: Button? = null
    private var homeButtonFcs: TextView? = null

    private var timer: CountDownTimer? = null
    private val focusTimeMillis: Long = 25 * 60 * 1000
    private val breakTimeMillis: Long = 5 * 60 * 1000
    private var timeLeftInMillis: Long = focusTimeMillis
    private var isBreakTime = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pomodoro)

        val userId = intent.getLongExtra("USER_ID", -1L)


        timerTextView = findViewById(R.id.timer)
        startButton = findViewById(R.id.start)
        endButton = findViewById(R.id.End)
        homeButtonFcs = findViewById(R.id.back_to_homebtn)

        updateTimerText()

        startButton?.setOnClickListener {
            startTimer()
            startButton?.visibility = Button.GONE
            endButton?.visibility = Button.VISIBLE
        }

        endButton?.setOnClickListener {
            timer?.cancel()
            resetTimer()
            startButton?.visibility = Button.VISIBLE
            endButton?.visibility = Button.GONE
        }

        homeButtonFcs?.setOnClickListener {
            val homeButtonFocus = Intent(this, HomePage::class.java)
            homeButtonFocus.putExtra("USER_ID", userId)
            startActivity(homeButtonFocus,  ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
            finish()
        }
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerText()
            }

            override fun onFinish() {
                if (!isBreakTime) {
                    Toast.makeText(this@focus, "Take a break!", Toast.LENGTH_SHORT).show()
                    isBreakTime = true
                    timeLeftInMillis = breakTimeMillis
                    startTimer() // Start 5-minute break timer
                } else {
                    isBreakTime = false
                    resetTimer() // Reset to 25 minutes after break
                }
            }
        }.start()
    }

    private fun resetTimer() {
        timeLeftInMillis = focusTimeMillis
        updateTimerText()
    }

    private fun updateTimerText() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        timerTextView.text = String.format("%02d:%02d", minutes, seconds)
    }
}