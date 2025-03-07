package com.example.lifeline24_7

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.chaquo.python.Python
import com.google.firebase.auth.FirebaseAuth


class Main_page : AppCompatActivity() {

    private val PREFS_NAME = "MyAppPrefs"
    private val FIRST_LAUNCH_KEY = "firstLaunch"
    private lateinit var auth: FirebaseAuth
    private lateinit var accidentcardView: CardView

    private lateinit var firecardView: CardView

    private lateinit var trafficcardView: CardView
    private lateinit var myinfo: CardView




    override fun onCreate(savedInstanceState: Bundle?) {
        val preferences: SharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val isFirstLaunch = preferences.getBoolean(FIRST_LAUNCH_KEY, true)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

            setContentView(R.layout.activity_main_page)
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Check if the user is already signed in
        val currentUser = auth.currentUser
        accidentcardView=findViewById(R.id.cardView)
        firecardView=findViewById(R.id.cardView2)
        trafficcardView=findViewById(R.id.cardView3)
        myinfo=findViewById(R.id.cardView4)


        if (currentUser != null) {
            // User is signed in, proceed to the home screen or main activity
            // You can access user details like currentUser.email, currentUser.uid, etc.
            navigateToHome()
        } else {
            // No user is signed in, proceed to the Sign-In activity
            navigateToSignIn()
        }
    }

    private fun navigateToHome() {
        // Navigate to HomeActivity or wherever you want
//        val intent = Intent(this, Accident_activity::class.java)
//        startActivity(intent)
//        finish() // Optionally finish this activity to prevent the user from coming back here

        val intent_accident=Intent(this,Accident_activity::class.java)
        val intent_fire=Intent(this,Activity_fire::class.java)

        val intent_info=Intent(this,UserInfoActivity::class.java)

        val intent_traffic=Intent(this,Activity_traffic_issue::class.java)
        accidentcardView.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
            startActivity(intent_accident)

            }

        })

        firecardView.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                startActivity(intent_fire)

            }


        })
        trafficcardView.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                startActivity(intent_traffic)

            }


        })

        myinfo.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                startActivity(intent_info)

            }


        })



    }

    private fun navigateToSignIn() {
        // Navigate to SignInActivity
        val intent = Intent(this, SigninActivity::class.java)
        startActivity(intent)
        finish() // Optionally finish this activity
    }
}



//
//
//
//        val py = Python.getInstance()
//
//
//        // Call the Python script
//        val module = py.getModule("example")
//
//        val num=module["number"]?.toInt()
//        println("The value of num is :$num")
//
//        val text=module["text"]?.toString()
//        println("The value of the text is : $text")
//        Log.d("TAG", "Text is: $text")
//
//
//        val fact=module["factorial"]
//        val a =fact?.call(5)
//        Log.d("Factorial", "Factorial is: $a")
//
//



