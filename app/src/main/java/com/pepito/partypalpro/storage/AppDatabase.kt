package com.pepito.partypalpro.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// AppDatabase.kt

@Database(entities = [Guest::class, Task::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun guestDao(): GuestDao
    abstract fun taskDao(): TaskDao

    companion object {
        private const val DATABASE_NAME = "party_pal_database"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
