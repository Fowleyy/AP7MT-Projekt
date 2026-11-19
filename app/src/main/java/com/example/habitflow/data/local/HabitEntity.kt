package com.example.habitflow.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    val description: String = "",

    val emoji: String = "🎯",

    val repeatType: String = "DAILY",

    val reminderTime: String? = null,

    val isActive: Boolean = true,

    val isCompleted: Boolean = false
)