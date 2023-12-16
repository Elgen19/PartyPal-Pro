package com.pepito.partypalpro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import com.pepito.partypalpro.databinding.ActivityAddAguestBinding
import com.pepito.partypalpro.storage.AppDatabase
import com.pepito.partypalpro.storage.Guest
import com.pepito.partypalpro.storage.GuestDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddAGuestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddAguestBinding
    private lateinit var guestDao: GuestDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAguestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the Room database
        val database = AppDatabase.getInstance(this)
        guestDao = database.guestDao()

        val addButton: Button = binding.buttonAddGuest
        addButton.setOnClickListener {
            addGuest()
        }

        // Set up ArrayAdapter for the Spinner using the array resource
        val spinnerArrayAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.rsvp_statuses,
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.spinnerRSVP.adapter = spinnerArrayAdapter
    }

    private fun addGuest() {
        val name = binding.editTextName.text.toString()
        val email = binding.editTextEmail.text.toString()
        val rsvpStatus = binding.spinnerRSVP.selectedItem.toString()

        if (name.isNotBlank() && email.isNotBlank()) {
            val guest = Guest(name = name, email = email, rsvpStatus = rsvpStatus)

            // Use a coroutine to perform the database operation on a background thread
            CoroutineScope(Dispatchers.IO).launch {
                val insertedId = guestDao.addGuest(guest)

                // Check if the insertion was successful
                if (insertedId > 0) {
                    // Insertion successful, log it
                    Log.d("AddGuestActivity", "Guest added successfully with ID: $insertedId")

                    // Optionally, you can handle it further or finish the activity
                    runOnUiThread {
                        Toast.makeText(
                            this@AddAGuestActivity,
                            "Guest added successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                } else {
                    // Insertion failed, log it
                    Log.e("AddGuestActivity", "Failed to add guest to the database")

                    runOnUiThread {
                        Toast.makeText(this@AddAGuestActivity, "Failed to add guest", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        } else {
            // Handle invalid input
            Toast.makeText(this, "Name and email are required", Toast.LENGTH_SHORT).show()
        }
    }
}
