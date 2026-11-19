package com.example.habitflow.data.local

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var INSTANCE: HabitDatabase? = null

    fun getDatabase(context: Context): HabitDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                HabitDatabase::class.java,
                "habit_database"
            ).build()

            INSTANCE = instance
            instance
        }
    }
}