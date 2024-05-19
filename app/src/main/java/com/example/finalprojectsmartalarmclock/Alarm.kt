package com.example.finalprojectsmartalarmclock

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time
import java.util.Date

@Entity(tableName = "alarms")
data class Alarm (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Date,
    val time: Date
)
