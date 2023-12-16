package com.pepito.partypalpro.storage

import androidx.room.Entity
import androidx.room.PrimaryKey

// Guest.kt
@Entity(tableName = "guests")
data class Guest(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var name: String,
    var email: String,
    var rsvpStatus: String
)

