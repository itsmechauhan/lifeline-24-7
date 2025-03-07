package com.example.lifeline24_7

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.content.Intent
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException


class SigninActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    // Views
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signinButton: Button
    private lateinit var signupTextView: TextView
    private lateinit var forgotPasswordTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Find views by ID
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        signinButton = findViewById(R.id.signin_button)
        signupTextView = findViewById(R.id.signup_text)
        forgotPasswordTextView = findViewById(R.id.forgot_password_text)

        // Sign In button click listener
        signinButton.setOnClickListener {
            signInUser()
        }

        // Redirect to Signup activity
        signupTextView.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        // Redirect to Forgot Password activity
        forgotPasswordTextView.setOnClickListener {
            // Implement forgot password functionality (if needed)
            // You can open a reset password screen here or show a dialog
            Toast.makeText(this, "Password reset functionality not implemented", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signInUser() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        // Validate input fields
        if (TextUtils.isEmpty(email)) {
            emailEditText.error = "Please enter your email"
            return
        }
        if (TextUtils.isEmpty(password)) {
            passwordEditText.error = "Please enter your password"
            return
        }

        // Sign in the user with Firebase Authentication
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in successful
                    Toast.makeText(this, "Sign In Successful", Toast.LENGTH_SHORT).show()
                    // You can redirect the user to another activity after sign-in, e.g., Home screen
                     startActivity(Intent(this, Main_page::class.java))
                  // Close the SignInActivity
                } else {
                    // Handle sign-in failure
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        // Invalid password
                        Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show()
                    } catch (e: FirebaseAuthInvalidUserException) {
                        // User not found
                        Toast.makeText(this, "No account found with this email", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        // General error
                        Toast.makeText(this, "Sign In Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
    }




