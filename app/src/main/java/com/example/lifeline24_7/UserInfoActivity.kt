package com.example.lifeline24_7

//import android.os.Bundle
import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class UserInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_info)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Get data from Intent
        val userName = intent.getStringExtra("USER_NAME") ?: "N/A"
        val userEmail = intent.getStringExtra("USER_EMAIL") ?: "N/A"

        // Find TextViews
        val nameTextView: TextView = findViewById(R.id.textViewUserName)
        val emailTextView: TextView = findViewById(R.id.textViewUserEmail)

        // Set data
        nameTextView.text = "Name: $userName"
        emailTextView.text = "Email: $userEmail"

    }
}



