package com.example.habitflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habitflow.data.local.DatabaseProvider
import com.example.habitflow.data.repository.HabitRepository
import com.example.habitflow.navigation.AppNavigation
import com.example.habitflow.viewmodel.HabitViewModel
import com.example.habitflow.viewmodel.HabitViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = DatabaseProvider.getDatabase(applicationContext)

        val repository = HabitRepository(
            habitDao = database.habitDao(),
            completionDao = database.habitCompletionDao()
        )

        val factory = HabitViewModelFactory(repository)

        setContent {
            val habitViewModel: HabitViewModel = viewModel(factory = factory)

            MaterialTheme {
                Surface {
                    AppNavigation(habitViewModel = habitViewModel)
                }
            }
        }
    }
}