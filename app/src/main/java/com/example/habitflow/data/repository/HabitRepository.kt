package com.example.habitflow.data.repository

import com.example.habitflow.data.local.HabitDao
import com.example.habitflow.data.local.HabitEntity
import com.example.habitflow.data.local.HabitCompletionDao
import com.example.habitflow.data.local.HabitCompletionEntity
import kotlinx.coroutines.flow.Flow

class HabitRepository(
    private val habitDao: HabitDao,
    private val completionDao: HabitCompletionDao
) {

    fun getAllHabits(): Flow<List<HabitEntity>> =
        habitDao.getAllHabits()

    suspend fun getHabitById(id: Long): HabitEntity? =
        habitDao.getHabitById(id)

    suspend fun insertHabit(habit: HabitEntity): Long =
        habitDao.insertHabit(habit)

    suspend fun updateHabit(habit: HabitEntity) =
        habitDao.updateHabit(habit)

    suspend fun deleteHabit(habit: HabitEntity) =
        habitDao.deleteHabit(habit)

    fun getAllCompletions(): Flow<List<HabitCompletionEntity>> =
        completionDao.getAllCompletions()

    fun getCompletionsForHabit(habitId: Long): Flow<List<HabitCompletionEntity>> =
        completionDao.getCompletionsForHabit(habitId)

    suspend fun insertCompletion(completion: HabitCompletionEntity) =
        completionDao.insertCompletion(completion)

    suspend fun deleteCompletion(habitId: Long, date: String) =
        completionDao.deleteCompletion(habitId, date)
}