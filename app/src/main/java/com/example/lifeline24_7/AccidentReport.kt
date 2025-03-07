package com.example.lifeline24_7

data class AccidentReport(
    val id: String = "",  // Add an ID field for Firestore document reference
    val date: String = "",
    val location: String = "",
    val description: String = "",
    val status: String = "" // To store report status (e.g., "pending", "approved")
)
