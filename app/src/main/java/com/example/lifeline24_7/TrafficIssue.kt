package com.example.lifeline24_7

data class TrafficIssue(
    val id: String = "",
    val description: String = "",
    val location: String = "",
    val issueType: String = "",
    val userId: String = "",
    val status: String = "pending", // Default status is "pending"
    val timestamp: Long = 0L,
    val imageUrl: String? = null
)
