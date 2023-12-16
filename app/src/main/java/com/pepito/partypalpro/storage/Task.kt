// Task.kt

package com.pepito.partypalpro.storage

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var taskName: String,
    var taskDescription: String,
    var taskStatus: String,
    var isChecked: Boolean = false,
    var isReminderSet: Boolean = false,
    var reminderHour: Int = 0,
    var reminderMinute: Int = 0
)
