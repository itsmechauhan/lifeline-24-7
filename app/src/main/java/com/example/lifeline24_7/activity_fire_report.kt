package com.example.lifeline24_7

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.app.Activity
import android.content.Intent
import android.net.Uri
//import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.util.UUID

class activity_fire_report : AppCompatActivity() {

    private lateinit var editDescription: EditText
    private lateinit var editLocation: EditText
    private lateinit var submitReportButton: Button
    private lateinit var btnUploadImage: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageViewPreview: ImageView

    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth

    private var imageUri: Uri? = null // URI for the uploaded image

    private lateinit var fireReportAdapter: FireReportAdapter
    private val fireReports = mutableListOf<FireReport>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_fire_report)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Initialize views
        editDescription = findViewById(R.id.editDescription)
        editLocation = findViewById(R.id.editLocation)
        submitReportButton = findViewById(R.id.submitReportButton)
        btnUploadImage = findViewById(R.id.btnUploadImage)
        recyclerView = findViewById(R.id.recyclerViewReports)
        imageViewPreview = findViewById(R.id.imageViewPreview)

        // Initialize Firebase
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        fireReportAdapter = FireReportAdapter(fireReports)
        recyclerView.adapter = fireReportAdapter

        // Upload image button
        btnUploadImage.setOnClickListener {
            pickImageFromGallery()
        }

        // Submit report button
        submitReportButton.setOnClickListener {
            val description = editDescription.text.toString().trim()
            val location = editLocation.text.toString().trim()

            // Validate inputs
            if (description.isEmpty() || location.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                if (imageUri != null) {
                    uploadImageAndSaveReport(description, location)
                } else {
                    saveReport(description, location, null) // No image
                }
            }
        }

        // Fetch reports from Firestore
        fetchFireReports()
    }

    // Fetch fire reports from Firestore
    private fun fetchFireReports() {
        firestore.collection("fireReports")
            .get()
            .addOnSuccessListener { result ->
                fireReports.clear()
                for (document in result) {
                    val description = document.getString("description") ?: ""
                    val location = document.getString("location") ?: ""
                    val imageUrl = document.getString("imageUrl")
                    val report = FireReport(description, location, imageUrl)
                    fireReports.add(report)
                }
                fireReportAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error fetching reports", Toast.LENGTH_SHORT).show()
            }
    }

    // Open gallery to pick an image
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    // Handle image selection result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            imageUri = data?.data
            imageViewPreview.setImageURI(imageUri) // Show selected image
        }
    }

    // Upload image to Firebase Storage and save report
    private fun uploadImageAndSaveReport(description: String, location: String) {
        if (imageUri != null) {
            val fileName = UUID.randomUUID().toString() // Unique filename
            val storageRef: StorageReference = storage.reference.child("fireReports/$fileName")

            storageRef.putFile(imageUri!!)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()
                        saveReport(description, location, imageUrl)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // Save report to Firestore
    private fun saveReport(description: String, location: String, imageUrl: String?) {
        val reportData = hashMapOf(
            "description" to description,
            "location" to location,
            "status" to "pending",
            "timestamp" to System.currentTimeMillis(),
            "imageUrl" to (imageUrl ?: "")
        )

        firestore.collection("fireReports")
            .add(reportData)
            .addOnSuccessListener {
                Toast.makeText(this, "Report submitted successfully", Toast.LENGTH_SHORT).show()
                clearForm()
                fetchFireReports()  // Refresh the list
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error submitting report", Toast.LENGTH_SHORT).show()
            }
    }

    // Clear form after submitting report
    private fun clearForm() {
        editDescription.text.clear()
        editLocation.text.clear()
        imageViewPreview.setImageResource(android.R.drawable.ic_menu_camera)
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
}


