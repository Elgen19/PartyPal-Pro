package com.pepito.partypalpro

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.pepito.partypalpro.databinding.ActivitySendInvitationBinding

class SendInvitationActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySendInvitationBinding
    private val sendEmailLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            // The email was sent successfully, navigate to MainActivity
            Toast.makeText(this, "Invitation successfully sent!", Toast.LENGTH_SHORT).show()
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendInvitationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSendInvitation.setOnClickListener {
            val recipient = binding.editTextRecipient.text.toString().trim()
            val subject = binding.editTextSubject.text.toString().trim()
            val message = binding.editTextMessage.text.toString().trim()

            if (recipient.isEmpty() || subject.isEmpty() || message.isEmpty()) {
                // Show error message if any field is empty
                // You can customize this based on your app's requirements
                // For simplicity, showing a toast message here
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Create an intent to send the invitation via email
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "message/rfc822" // Email MIME type
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
                intent.putExtra(Intent.EXTRA_SUBJECT, subject)
                intent.putExtra(Intent.EXTRA_TEXT, message)
                sendEmailLauncher.launch(Intent.createChooser(intent, "Send Invitation"))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK || resultCode == RESULT_CANCELED) {
            // Treat both RESULT_OK and RESULT_CANCELED as successful email sending
            Toast.makeText(this, "Invitation successfully sent!", Toast.LENGTH_SHORT).show()
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
            finish()
        } else {
            // If the email sending was failed for some reason
            Toast.makeText(this, "Email sending failed or canceled", Toast.LENGTH_SHORT).show()
        }
    }


}
