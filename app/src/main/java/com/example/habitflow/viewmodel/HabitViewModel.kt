package com.example.habitflow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitflow.data.local.HabitEntity
import com.example.habitflow.data.local.HabitCompletionEntity
import com.example.habitflow.data.repository.HabitRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class HabitViewModel(
    private val repository: HabitRepository
) : ViewModel() {

    val habits: StateFlow<List<HabitEntity>> =
        repository.getAllHabits().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val completions: StateFlow<List<HabitCompletionEntity>> =
        repository.getAllCompletions().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addHabit(
        name: String,
        description: String = "",
        emoji: String = "🎯",
        repeatType: String = "DAILY",
        reminderTime: String? = null
    ) {
        viewModelScope.launch {
            repository.insertHabit(
                HabitEntity(
                    name = name,
                    description = description,
                    emoji = emoji,
                    repeatType = repeatType,
                    reminderTime = reminderTime
                )
            )
        }
    }

    fun updateHabit(habit: HabitEntity) {
        viewModelScope.launch {
            repository.updateHabit(habit)
        }
    }

    fun deleteHabit(habit: HabitEntity) {
        viewModelScope.launch {
            repository.deleteHabit(habit)
        }
    }

    fun toggleHabitCompletion(habit: HabitEntity) {
        viewModelScope.launch {
            val today = LocalDate.now().toString()

            if (habit.isCompleted) {
                repository.deleteCompletion(habit.id, today)
            } else {
                repository.insertCompletion(
                    HabitCompletionEntity(
                        habitId = habit.id,
                        date = today
                    )
                )
            }

            repository.updateHabit(
                habit.copy(isCompleted = !habit.isCompleted)
            )
        }
    }
}