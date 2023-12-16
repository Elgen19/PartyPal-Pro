package com.pepito.partypalpro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pepito.partypalpro.databinding.ActivityGuestDetailBinding
import com.pepito.partypalpro.storage.AppDatabase
import com.pepito.partypalpro.storage.GuestDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GuestDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGuestDetailBinding
    private lateinit var guestDao: GuestDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuestDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the Room database
        val database = AppDatabase.getInstance(this)
        guestDao = database.guestDao()

        // Get the guest ID from the intent
        val guestId = intent.getLongExtra("guestId", -1)

        if (guestId != -1L) {
            // Use Coroutines to retrieve the guest details from the database
            CoroutineScope(Dispatchers.Main).launch {
                val guest = guestDao.getGuestById(guestId)

                // Populate the views with guest details
                binding.editTextName.setText(guest?.name)
                binding.editTextEmail.setText(guest?.email)

                // Set the selection for the spinner based on the guest's RSVP status
                val statusArray = resources.getStringArray(R.array.rsvp_statuses)
                val statusIndex = statusArray.indexOf(guest?.rsvpStatus)
                if (statusIndex != -1) {
                    binding.spinnerRSVP.setSelection(statusIndex)
                }
            }

        }

        // Set up click listeners for update and delete buttons
        binding.buttonDeleteGuest.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                // Get the guest ID from the intent
                val guestIdVal = intent.getLongExtra("guestId", -1)

                if (guestId != -1L) {
                    // Use Coroutines to delete the guest from the database
                    guestDao.deleteGuestById(guestIdVal)

                    // Optionally, you can navigate back to the main activity or perform other actions
                    finish()
                }
            }
        }


        binding.buttonUpdateGuest.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                // Get the guest ID from the intent
                val guestIdVal = intent.getLongExtra("guestId", -1)

                if (guestId != -1L) {
                    // Use Coroutines to retrieve the guest details from the database
                    val guest = guestDao.getGuestById(guestIdVal)

                    // Update guest details with the new values
                    guest?.name = binding.editTextName.text.toString()
                    guest?.email = binding.editTextEmail.text.toString()
                    guest?.rsvpStatus = binding.spinnerRSVP.selectedItem.toString()

                    // Use Coroutines to update the guest in the database
                    guest?.let {
                        guestDao.updateGuest(it)

                        // Optionally, you can navigate back to the main activity or perform other actions
                        finish()
                    }
                }
            }
        }

    }
}
