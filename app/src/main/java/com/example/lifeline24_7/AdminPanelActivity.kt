package com.example.lifeline24_7

import android.os.Bundle
import androidx.activity.enableEdgeToEdge

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class AdminPanelActivity : AppCompatActivity() {
    private lateinit var reportsRecyclerView: RecyclerView
    private lateinit var reportsList: MutableList<AccidentReport>
    private lateinit var reportsAdapter: ReportsAdapter

    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_panel)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        reportsRecyclerView = findViewById(R.id.reportsRecyclerView)
        reportsRecyclerView.layoutManager = LinearLayoutManager(this)

        reportsList = mutableListOf()
        reportsAdapter = ReportsAdapter(reportsList, ::approveReport, ::deleteReport)

        reportsRecyclerView.adapter = reportsAdapter


        fetchReports()
    }

    private fun fetchReports() {
        db.collection("accident_reports")
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val reports = documents.toObjects(AccidentReport::class.java)
                    reportsList.clear()
                    reportsList.addAll(reports)
                    reportsAdapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error getting reports: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Approve Report function
    private fun approveReport(report: AccidentReport) {
        val reportRef = db.collection("accident_reports").document(report.id)

        // Update the report status to "approved"
        reportRef.update("status", "approved")
            .addOnSuccessListener {
                Toast.makeText(this, "Report Approved", Toast.LENGTH_SHORT).show()
                fetchReports()  // Refresh the list after approving
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error approving report: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Delete Report function
    private fun deleteReport(report: AccidentReport) {
        val reportRef = db.collection("accident_reports").document(report.id)

        // Delete the report
        reportRef.delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Report Deleted", Toast.LENGTH_SHORT).show()
                fetchReports()  // Refresh the list after deletion
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error deleting report: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }


    }
