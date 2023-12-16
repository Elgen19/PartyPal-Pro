package com.pepito.partypalpro.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

// GuestDao.kt
@Dao
interface GuestDao {

    @Insert
    suspend fun addGuest(guest: Guest): Long

    @Query("SELECT * FROM guests")
    suspend fun getAllGuests(): List<Guest>

    @Query("SELECT * FROM guests WHERE id = :guestId")
    suspend fun getGuestById(guestId: Long): Guest?

    @Query("DELETE FROM guests WHERE id = :guestId")
    suspend fun deleteGuestById(guestId: Long)

    @Update
    suspend fun updateGuest(guest: Guest)
}
