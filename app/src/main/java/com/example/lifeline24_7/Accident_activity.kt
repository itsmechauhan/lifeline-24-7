package com.example.lifeline24_7


import androidx.activity.enableEdgeToEdge

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class Accident_activity : AppCompatActivity() {

    private lateinit var accidentDateEditText: EditText
    private lateinit var locationTextView: TextView
    private lateinit var descriptionEditText: EditText
    private lateinit var attachPhotoButton: Button
    private lateinit var submitButton: Button

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val firestore = FirebaseFirestore.getInstance() // Firestore instance

    companion object {
        const val REQUEST_LOCATION_PERMISSION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_accident)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Initialize views
        accidentDateEditText = findViewById(R.id.accident_date)
        locationTextView = findViewById(R.id.location_text_view)
        descriptionEditText = findViewById(R.id.description)
        attachPhotoButton = findViewById(R.id.attach_photo_button)
        submitButton = findViewById(R.id.submit_button)

        // Set default date
        val currentDate = java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        accidentDateEditText.setText(currentDate)

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Check permissions and get location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permission
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
        } else {
            getCurrentLocation()
        }

        // Set submit button listener
        submitButton.setOnClickListener {
            submitReport()
        }
    }

    private fun getCurrentLocation() {
        // Get last known location
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    // Location found
                    val latitude = location.latitude
                    val longitude = location.longitude

                    // Use Geocoder to get the address
                    val geocoder = Geocoder(this, Locale.getDefault())
                    val addresses = geocoder.getFromLocation(latitude, longitude, 1)

                    if (addresses != null && addresses.isNotEmpty()) {
                        // Get the address from Geocoder
                        val address = addresses[0].getAddressLine(0)
                        locationTextView.text = "Location: $address"
                    }
                } else {
                    Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to get location", Toast.LENGTH_SHORT).show()
            }
    }

    private fun submitReport() {
        val date = accidentDateEditText.text.toString().trim()
        val location = locationTextView.text.toString().trim()
        val description = descriptionEditText.text.toString().trim()

        // Validate inputs
        if (date.isEmpty() || location.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a report map
        val report = hashMapOf(
            "date" to date,
            "location" to location,
            "description" to description,
            "status" to "pending",
            "timestamp" to System.currentTimeMillis() // Store timestamp of the report
        )

        // Save report to Firestore
        firestore.collection("accident_reports")
            .add(report)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Accident Report Submitted Successfully", Toast.LENGTH_SHORT).show()
                Log.d("AccidentReport", "Report saved with ID: ${documentReference.id}")
                finish() // Close the activity after submitting
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error submitting report", Toast.LENGTH_SHORT).show()
                Log.w("AccidentReport", "Error adding document", e)
            }
    }

    // Handle permission request result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                getCurrentLocation()
            } else {
                Toast.makeText(this, "Location permission required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    }


