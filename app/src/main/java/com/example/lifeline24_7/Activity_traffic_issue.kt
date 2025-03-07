package com.example.lifeline24_7

import android.os.Bundle
import android.app.Activity
import android.content.Intent
import android.widget.*
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Activity_traffic_issue : AppCompatActivity() {

    private lateinit var editDescription: EditText
    private lateinit var editLocation: EditText
    private lateinit var spinnerIssueType: Spinner
    private lateinit var submitReportButton: Button

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_traffic_issue)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views
        editDescription = findViewById(R.id.editDescription)
        editLocation = findViewById(R.id.editLocation)
//        spinnerIssueType = findViewById(R.id.spinnerIssueType)
        submitReportButton = findViewById(R.id.submitReportButton)

        // Initialize Firebase
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Populate the spinner with issue types (e.g., Road under construction, High traffic, etc.)
//        val issueTypes = listOf("Road Under Construction", "High Traffic", "Accident", "Road Maintenance")
//        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, issueTypes)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinnerIssueType.adapter = adapter

        // Submit report button click listener
        submitReportButton.setOnClickListener {
            val description = editDescription.text.toString().trim()
            val location = editLocation.text.toString().trim()
//            val issueType = spinnerIssueType.selectedItem.toString()

            // Validate inputs
            if (description.isEmpty() || location.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, Main_page::class.java)

                // Save the report to Firebase Firestore
                saveReport(description, location)
                startActivity(intent)
            }
        }
    }

    // Save traffic issue data to Firestore
    private fun saveReport(description: String, location: String) {
        val user = auth.currentUser
        val userId = user?.uid ?: ""

        val reportData = hashMapOf(
            "description" to description,
            "location" to location,

            "userId" to userId,
            "status" to "pending", // Can be updated later (e.g., approved, resolved)
            "timestamp" to System.currentTimeMillis()
        )

        firestore.collection("trafficIssues")
            .add(reportData)
            .addOnSuccessListener {
                Toast.makeText(this, "Traffic issue reported successfully", Toast.LENGTH_SHORT).show()
                clearForm()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error submitting report: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Clear the form after submission
    private fun clearForm() {
        editDescription.text.clear()
        editLocation.text.clear()

    }
}
