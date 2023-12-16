package com.pepito.partypalpro.alarm

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.pepito.partypalpro.R

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // Handle the alarm here
        val taskName = intent?.getStringExtra("taskName")
        // You can show a notification, play a sound, etc.
        // For simplicity, let's just show a toast
        Toast.makeText(context, "Alarm for task: $taskName", Toast.LENGTH_LONG).show()
        showNotification(context, taskName)
        playNotificationSound(context)
    }

    private fun playNotificationSound(context: Context?) {
        // You can customize the sound by providing a URI or use a default sound
        val notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val ringtone = RingtoneManager.getRingtone(context, notificationSoundUri)
        ringtone.play()
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(context: Context?, taskName: String?) {
        createNotificationChannel(context)

        val builder = NotificationCompat.Builder(context!!, "default_channel")
            .setSmallIcon(R.drawable.task_icon)
            .setContentTitle("Task Reminder")
            .setContentText("Don't forget to do: $taskName")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            notify(1, builder.build())
        }
    }

    private fun createNotificationChannel(context: Context?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Default Channel"
            val descriptionText = "Default Notification Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("default_channel", name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
