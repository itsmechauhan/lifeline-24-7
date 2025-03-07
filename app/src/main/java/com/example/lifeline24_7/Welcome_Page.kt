package com.example.lifeline24_7

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Welcome_Page : AppCompatActivity() {
    private lateinit var get_started_btn: Button
    private val PREFS_NAME = "MyAppPrefs"
    private val FIRST_LAUNCH_KEY = "firstLaunch"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        val preferences: SharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val isFirstLaunch = preferences.getBoolean(FIRST_LAUNCH_KEY, true)
        Log.d("First launch", "isFirstLaunch: $isFirstLaunch")


        val next=Intent(this,Main_page::class.java)
        Handler(Looper.getMainLooper()).postDelayed(
            {
                startActivity(next)
                finish()
            }, 5000
        )




        if (isFirstLaunch) {
            // If it's the first launch, navigate to WelcomeActivity (or any activity you want)


            // Set the flag to false so the app doesn't show this again in the future
            val editor = preferences.edit()
            editor.putBoolean(FIRST_LAUNCH_KEY, false)
            editor.apply()

            // Close the current activity so the user can't go back to it
            finish()
        } else {
            // If it's not the first launch, continue to the normal MainActivity flow
//            setContentView(R.layout.activity_main)


            setContentView(R.layout.activity_welcome_page)
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

        }
        val inext = Intent(this, Main_page::class.java)
        Handler(Looper.getMainLooper()).postDelayed(
            {
                startActivity(inext)
                finish()
            }, 3000
        )



    }
}