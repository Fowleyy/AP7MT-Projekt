package com.example.habitflow.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseProvider {

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS habit_completions (
                    habitId INTEGER NOT NULL,
                    date TEXT NOT NULL,
                    PRIMARY KEY(habitId, date),
                    FOREIGN KEY(habitId) REFERENCES habits(id)
                    ON UPDATE NO ACTION ON DELETE CASCADE
                )
                """.trimIndent()
            )

            db.execSQL(
                """
                CREATE INDEX IF NOT EXISTS index_habit_completions_habitId
                ON habit_completions(habitId)
                """.trimIndent()
            )
        }
    }

    @Volatile
    private var INSTANCE: HabitDatabase? = null

    fun getDatabase(context: Context): HabitDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                HabitDatabase::class.java,
                "habit_database"
            )
                .addMigrations(MIGRATION_1_2)
                .build()

            INSTANCE = instance
            instance
        }
    }
}