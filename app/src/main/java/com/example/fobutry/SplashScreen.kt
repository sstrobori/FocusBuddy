package com.example.fobutry

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class SplashScreen  : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState:Bundle ?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splashscreen)


        val getStartedButton:MaterialButton = findViewById(R.id.get_startedbtn)
        val loginButton:TextView = findViewById(R.id.loginbtn)

        getStartedButton.setOnClickListener {
            val cr = Intent(this, CreateAccount::class.java)
            startActivity(cr)
            finish()
        }

        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        dbHelper = DatabaseHelper(this)


    }

}
