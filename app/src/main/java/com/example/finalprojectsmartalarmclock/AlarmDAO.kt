package com.example.finalprojectsmartalarmclock

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AlarmDAO {
    @Insert
    suspend fun insert(alarm: Alarm)

    @Query("SELECT * FROM alarms WHERE id = :id")
    suspend fun getAlarmById(id: Int): Alarm?

    @Query("SELECT * FROM alarms")
    suspend fun getAllAlarms(): List<Alarm>

    @Query("DELETE FROM alarms WHERE id = :id")
    suspend fun deleteById(id: Int)
}

