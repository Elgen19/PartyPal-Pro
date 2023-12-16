package com.pepito.partypalpro

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pepito.partypalpro.alarm.AlarmReceiver
import com.pepito.partypalpro.databinding.ActivityTaskDetailBinding
import com.pepito.partypalpro.storage.AppDatabase
import com.pepito.partypalpro.storage.TaskDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

class TaskDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskDetailBinding
    private lateinit var taskDao: TaskDao
    private var taskId: Long = -1
    private var isReminderSet = false
    private var reminderHour = 0
    private var reminderMinute = 0
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the Room database
        val database = AppDatabase.getInstance(this)
        taskDao = database.taskDao()

        // Get the task ID from the intent
        taskId = intent.getLongExtra("taskId", -1)

        if (taskId != -1L) {
            // Use Coroutines to retrieve the task details from the database
            CoroutineScope(Dispatchers.Main).launch {
                val task = taskDao.getTaskById(taskId)

                // Populate the views with task details
                binding.editTaskName.setText(task?.taskName)
                binding.editTextTaskDesc.setText(task?.taskDescription)
                // Set the switch state based on the saved state in the Task model
                binding.switchSetReminder.isChecked = task?.isReminderSet ?: false

                isReminderSet = task?.isReminderSet ?: false

                if (!isReminderSet){
                    binding.textViewReminderTime.text = getString(R.string.no_time_set)
                }
                else{
                    reminderHour = task?.reminderHour ?: 0
                    reminderMinute = task?.reminderMinute ?: 0
                    val setupTime = formatTime(reminderHour, reminderMinute)
                    binding.textViewReminderTime.text = getString(R.string.reminder_message, setupTime)
                }


            }
        }

        // Set up click listeners for update and delete buttons
        binding.buttonDeleteTask.setOnClickListener {
            // Handle delete action here
            deleteTask()
        }

        binding.buttonUpdateTask.setOnClickListener {
            // Handle update action here
            updateTask()
        }

       binding.switchSetReminder.setOnCheckedChangeListener { _, isChecked ->
           isReminderSet = isChecked

           if (!isReminderSet){
               binding.textViewReminderTime.text = getString(R.string.no_time_set)
               cancelAlarm()
               Toast.makeText(this, "Please press update button to apply changes.", Toast.LENGTH_LONG).show()
           }
        }

        binding.setAlarmButton.setOnClickListener {
            showTimePickerDialog()
        }


        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

    }

    @SuppressLint("ScheduleExactAlarm")
    private fun setAlarm() {
        if (isReminderSet) {
            val currentTime = Calendar.getInstance()
            val alarmTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, reminderHour)
                set(Calendar.MINUTE, reminderMinute)
                set(Calendar.SECOND, 0)
            }

            // Check if the selected time is in the future
            if (alarmTime.after(currentTime)) {
                val intent = Intent(this, AlarmReceiver::class.java)
                intent.putExtra("taskName", binding.editTaskName.text.toString())
                pendingIntent = PendingIntent.getBroadcast(
                    this,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // For devices running Android M (API 23) and above
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        alarmTime.timeInMillis,
                        pendingIntent
                    )
                } else {
                    // For devices running Android L (API 21) to K (API 19)
                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        alarmTime.timeInMillis,
                        pendingIntent
                    )
                }
            }
        }
    }


    private fun deleteTask() {
        CoroutineScope(Dispatchers.IO).launch {
            // Delete the task from the Room database
            taskDao.deleteTaskById(taskId)

            // Notify the user if needed
            // ...

            // Finish the activity (optional: navigate back to ToDoListActivity)
            finish()
        }
        Toast.makeText(this, "Task has been removed.", Toast.LENGTH_LONG).show()
    }

    private fun updateTask() {
        val updatedTaskName = binding.editTaskName.text.toString()
        val updatedTaskDesc = binding.editTextTaskDesc.text.toString()

        if (updatedTaskName.isNotBlank()) {
            CoroutineScope(Dispatchers.IO).launch {
                // Retrieve the existing task from the Room database
                val existingTask = taskDao.getTaskById(taskId)

                // Update the task details
                existingTask?.let {
                    it.taskName = updatedTaskName
                    it.taskDescription = updatedTaskDesc
                    it.isReminderSet = isReminderSet
                    it.reminderHour = reminderHour
                    it.reminderMinute = reminderMinute

                    // Keep the existing status and isChecked, do not update them here
                    // The status and isChecked will be updated in the RecyclerView in ToDoListActivity

                    // Update the entire task in the Room database
                    taskDao.updateTask(it)
                }

                // Call setAlarm to schedule the alarm
                setAlarm()

                // Finish the activity (optional: navigate back to ToDoListActivity)
                finish()
            }

            Toast.makeText(this, "Task update successful", Toast.LENGTH_LONG).show()
        } else {
            // Show an error message or handle empty task name
            // ...
        }
    }



    private fun showTimePickerDialog() {
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                onTimeSet(selectedHour, selectedMinute)
            },
            hour,
            minute,
            DateFormat.is24HourFormat(this)
        )

        timePickerDialog.show()
    }

    private fun onTimeSet(hourOfDay: Int, minute: Int) {
        reminderHour = hourOfDay
        reminderMinute = minute

        // Format the time
        val formattedTime = formatTime(hourOfDay, minute)

        // Set the text using the resource string with a placeholder
        binding.textViewReminderTime.text = getString(R.string.reminder_message, formattedTime)
    }

    private fun formatTime(hourOfDay: Int, minute: Int): String {
        val is24HourFormat = DateFormat.is24HourFormat(this)

        val formattedHour = if (is24HourFormat) {
            String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
        } else {
            // Convert 24-hour format to 12-hour format
            val amPm = if (hourOfDay < 12) "AM" else "PM"
            val formattedHour12 = if (hourOfDay % 12 == 0) 12 else hourOfDay % 12
            String.format(Locale.getDefault(), "%02d:%02d %s", formattedHour12, minute, amPm)
        }

        return formattedHour
    }

    private fun cancelAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
    }








}
