package com.example.lifeline24_7

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.content.Intent

import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
class AdminSignupActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signupButton: Button
    private lateinit var signInRedirectText: TextView
    private lateinit var token_admin:EditText
    private lateinit var admin_button:TextView
    private  val ADMIN_TOKEN="100"
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_signup)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        signupButton = findViewById(R.id.signupButton)
        signInRedirectText = findViewById(R.id.signInRedirectText)
        token_admin=findViewById(R.id.token_admin)
        admin_button=findViewById(R.id.admin_button)

        auth = FirebaseAuth.getInstance()

        signupButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val token=this.token_admin.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }else if(token!= ADMIN_TOKEN){
                Toast.makeText(this, "Invalid Token!!", Toast.LENGTH_SHORT).show()
            }
            else {
                createAdminAccount(email, password)
            }
        }

        // Redirect to Sign In Activity if the admin already has an account
        signInRedirectText.setOnClickListener {
            val intent = Intent(this, activity_admin_login::class.java)
            startActivity(intent)
        }
        admin_button.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createAdminAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registration was successful
                    val user = auth.currentUser

                    // Optionally store the admin details in Firestore
                    storeAdminData(user?.uid, email)

                    // Navigate to Admin Panel or Home Activity
                    val intent = Intent(this, AdminPanelActivity::class.java)
                    startActivity(intent)
                    finish()

                    Toast.makeText(this, "Admin Account Created", Toast.LENGTH_SHORT).show()
                } else {
                    // Registration failed
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun storeAdminData(userId: String?, email: String) {
        if (userId != null) {
            val adminData = hashMapOf(
                "uid" to userId,
                "email" to email,
                "role" to "admin"  // Assign the role of the user as "admin"
            )

            // Save admin data in Firestore
            db.collection("admins").document(userId)
                .set(adminData)
                .addOnSuccessListener {
                    // Admin data successfully stored
                    Toast.makeText(this, "Admin data stored successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    // Failed to store admin data
                    Toast.makeText(this, "Error storing admin data: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    }





