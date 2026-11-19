package com.example.habitflow.model

data class Habit(
    val id: Long = 0,
    val name: String,
    val description: String = "",
    val emoji: String = "🎯",
    val repeatType: RepeatType = RepeatType.DAILY,
    val reminderTime: String? = null,
    val isActive: Boolean = true,
    val isCompleted: Boolean = false
)

enum class RepeatType {
    DAILY,
    WEEKLY,
    ONCE
}