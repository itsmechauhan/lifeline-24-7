package com.example.lifeline24_7

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
//import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID
class Activity_fire : AppCompatActivity() {

    private lateinit var editDescription: EditText
    private lateinit var editLocation: EditText
    private lateinit var submitReportButton: Button
    private lateinit var btnUploadImage: Button
    private lateinit var imageViewPreview: ImageView

    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth

    private var imageUri: Uri? = null // URI of the image to upload
    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_fire)
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
        imageViewPreview = findViewById(R.id.imageViewPreview)

        // Initialize Firebase
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()

        // Upload image button click listener
        btnUploadImage.setOnClickListener {
            pickImageFromGallery()
        }

        // Submit report button click listener
        submitReportButton.setOnClickListener {
            val description = editDescription.text.toString().trim()
            val location = editLocation.text.toString().trim()

            // Validate inputs
            if (description.isEmpty() || location.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                val intent=Intent(this,Main_page::class.java)
                if (imageUri != null) {
                    uploadImageAndSaveReport(description, location)


                } else {
                    saveReport(description, location, null) // No image
                }
                startActivity(intent)
            }
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

    // Upload image to Firebase Storage and save report to Firestore
    private fun uploadImageAndSaveReport(description: String, location: String) {
        if (imageUri != null) {
            val fileName = UUID.randomUUID().toString() // Generate unique filename
            val storageRef: StorageReference = storage.reference.child("fireReports/$fileName")

            // Upload image
            storageRef.putFile(imageUri!!)
                .addOnSuccessListener {
                    // Get image download URL
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

    // Save report data to Firestore
    private fun saveReport(description: String, location: String, imageUrl: String?) {
        val user = auth.currentUser
        val userId = user?.uid ?: ""

        val reportData = hashMapOf(
            "description" to description,
            "location" to location,
            "userId" to userId,
            "status" to "pending",
            "timestamp" to System.currentTimeMillis(),
            "imageUrl" to (imageUrl ?: "") // Add image URL if available
        )

        firestore.collection("fireReports")
            .add(reportData)
            .addOnSuccessListener {
                Toast.makeText(this, "Report submitted successfully", Toast.LENGTH_SHORT).show()
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
        imageViewPreview.setImageResource(android.R.drawable.ic_menu_camera) // Reset image preview
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1 // Request code for image picker
    }
}





