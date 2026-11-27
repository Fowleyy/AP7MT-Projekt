package com.example.habitflow.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitCompletionDao {

    @Query("SELECT * FROM habit_completions ORDER BY date ASC")
    fun getAllCompletions(): Flow<List<HabitCompletionEntity>>

    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId")
    fun getCompletionsForHabit(
        habitId: Long
    ): Flow<List<HabitCompletionEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCompletion(
        completion: HabitCompletionEntity
    )

    @Query(
        "DELETE FROM habit_completions " +
                "WHERE habitId = :habitId AND date = :date"
    )
    suspend fun deleteCompletion(
        habitId: Long,
        date: String
    )
}