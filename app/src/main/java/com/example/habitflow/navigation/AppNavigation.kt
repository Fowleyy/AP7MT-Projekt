package com.example.habitflow.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.habitflow.viewmodel.HabitViewModel
import com.example.habitflow.ui.screens.HabitsScreen

@Composable
fun AppNavigation(
    habitViewModel: HabitViewModel
) {
    val navController = rememberNavController()

    val screens = listOf(
        Screen("home", "Domů", "🏠"),
        Screen("habits", "Návyky", "✅"),
        Screen("statistics", "Statistiky", "📊"),
        Screen("calendar", "Kalendář", "📅"),
        Screen("settings", "Nastavení", "⚙️")
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry =
                    navController.currentBackStackEntryAsState()

                val currentRoute =
                    navBackStackEntry.value?.destination?.route

                screens.forEach { screen ->
                    NavigationBarItem(
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Text(screen.icon)
                        },
                        label = {
                            Text(screen.label)
                        }
                    )
                }
            }
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            screens.forEach { screen ->
                composable(screen.route) {
                    if (screen.route == "habits") {
                        HabitsScreen(habitViewModel)
                    } else {
                        PlaceholderScreen(screen.label)
                    }
                }
            }
        }
    }
}

data class Screen(
    val route: String,
    val label: String,
    val icon: String
)

@Composable
fun PlaceholderScreen(title: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = title)
    }
}