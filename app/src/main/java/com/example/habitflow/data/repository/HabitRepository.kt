package com.example.habitflow.data.repository

import com.example.habitflow.data.local.HabitDao
import com.example.habitflow.data.local.HabitEntity
import kotlinx.coroutines.flow.Flow

class HabitRepository(
    private val habitDao: HabitDao
) {

    fun getAllHabits(): Flow<List<HabitEntity>> {
        return habitDao.getAllHabits()
    }

    suspend fun getHabitById(id: Long): HabitEntity? {
        return habitDao.getHabitById(id)
    }

    suspend fun insertHabit(habit: HabitEntity): Long {
        return habitDao.insertHabit(habit)
    }

    suspend fun updateHabit(habit: HabitEntity) {
        habitDao.updateHabit(habit)
    }

    suspend fun deleteHabit(habit: HabitEntity) {
        habitDao.deleteHabit(habit)
    }
}