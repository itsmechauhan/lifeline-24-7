package com.example.lifeline24_7

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class SignupActivity : AppCompatActivity() {
    // Firebase Auth instance
    private lateinit var auth: FirebaseAuth

    // Views
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var signupButton: Button
    private lateinit var loginTextView: TextView
    private lateinit var admin_button:TextView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        auth = FirebaseAuth.getInstance()

        usernameEditText = findViewById(R.id.username)
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        confirmPasswordEditText = findViewById(R.id.confirm_password)
        signupButton = findViewById(R.id.signup_button)
        loginTextView = findViewById(R.id.login_text)
        admin_button=findViewById(R.id.admin_button)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Set up the sign-up button listener
        signupButton.setOnClickListener {
            signUpUser()
        }

        // Set up the login text click listener
        loginTextView.setOnClickListener {
            // You can redirect the user to login activity
             startActivity(Intent(this, SigninActivity::class.java))
//            finish()  // For now, we just close the signup activity.
        }
        admin_button.setOnClickListener {
            // You can redirect the user to login activity
            startActivity(Intent(this, AdminSignupActivity::class.java))
//            finish()  // For now, we just close the signup activity.
        }
    }

    private fun signUpUser() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val confirmPassword = confirmPasswordEditText.text.toString().trim()

        // Check for empty fields
        if (TextUtils.isEmpty(email)) {
            emailEditText.error = "Please enter your email"
            return
        }
        if (TextUtils.isEmpty(password)) {
            passwordEditText.error = "Please enter your password"
            return
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordEditText.error = "Please confirm your password"
            return
        }
        if (password != confirmPassword) {
            confirmPasswordEditText.error = "Passwords do not match"
            return
        }

        // Create the user with email and password using Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign up success
                    Toast.makeText(this, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                    // You can redirect the user to another activity after successful signup
                    startActivity(Intent(this, SigninActivity::class.java))
                    finish()  // Close the signup activity
                } else {
                    // Sign up failure
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthUserCollisionException) {
                        // User already exists
                        Toast.makeText(this, "User already exists. Try logging in.", Toast.LENGTH_SHORT).show()
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        // Invalid email format
                        Toast.makeText(this, "Invalid email format.", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        // Other errors
                        Toast.makeText(this, "Sign Up Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
    }
